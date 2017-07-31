# -*- coding: utf-8 -*-
import scrapy
from hashlib import md5
from json import dumps
from datetime import datetime
import grequests as greq
from scrapy import Selector

INDEX_NAME = 't:twitter'
DEFAULT_IMG = '<uk>'


class TwitterSpider(scrapy.Spider):
    name = 'twitter_posts'

    def start_requests(self):
        start_links = [
            'https://twitter.com/XHNews',
            'https://twitter.com/ftrain',
            'https://twitter.com/_gamemix_',
            'https://twitter.com/bobbyclee',
            'https://twitter.com/lopp',
            'https://twitter.com/AyrtonGsZ',
            'https://twitter.com/nytimes/status/891722728683786240',
            'https://twitter.com/search?q=bitcoin&src=typd',
            'https://twitter.com/search?q=cryptocurrency&src=typd',
            'https://twitter.com/search?q=news&src=typd',
            'https://twitter.com/search?f=news&vertical=default&q=cryptocurrency&src=typd'
        ]

        for url in start_links:
            r = scrapy.Request(url=url, callback=self.parse)
            yield r

    def parse(self, response):
        tweets = response.css('.stream-items li.stream-item')
        ary = []
        for tw in tweets:
            post = self._domToPost(tw)
            ary.append(post)
            yield post

        self._createPost(ary)

    def _createPost(self, ary):
        url = 'http://127.0.0.1:5000/api/post/new'
        data = dumps(ary)
        p = greq.post(url, data=data,
             headers={'Content-type': 'application/json'})
        greq.map([p])

    def _domToPost(self, tw):
        account = tw.css('.content a.account-group')[0]

        tweet_id = tw.css('::attr("data-item-id")').extract_first()

        tweet_username = tw.css('.content .username b::text') \
            .extract_first()

        post_images = tw.css('.content .AdaptiveMediaOuterContainer ' +
                             'img::attr("src")') \
            .extract()

        post_videos = tw.css('.content .AdaptiveMediaOuterContainer ' +
                             '.PlayableMedia-player::attr("style")') \
            .re(r'\((.*)\)')

        post_url = 'https://twitter.com' + \
            tw.css('.tweet::attr("data-permalink-path")') \
            .extract_first()

        post_description = self.get_tweet_description(tw)

        ts = tw.css('.tweet-timestamp ' +
                    'span._timestamp::attr("data-time-ms")') \
            .extract_first()

        post_date = datetime.utcfromtimestamp(long(ts)/1000.0) \
            .strftime('%Y-%m-%dT%H:%M:%S')

        replies = tw.css('.ProfileTweet-actionCountForPresentation')[0] \
            .css('::text') \
            .extract_first()
        retweet = tw.css('.ProfileTweet-actionCountForPresentation')[1] \
            .css('::text') \
            .extract_first()
        love = tw.css('.ProfileTweet-actionCountForPresentation')[2] \
            .css('::text') \
            .extract_first()

        tweet_data = {
            "who": account.css('.FullNameGroup .fullname::text')
            .extract_first(),
            "username": tweet_username,
            "action": "tweet",
            "replies": int(replies if replies else 0),
            "retweets": int(retweet if retweet else 0),
            "love": int(love if love else 0),
            "images": post_images,
            "videos": post_videos,
            "tweetID": tweet_id
        }

        tw.css('.content .ProfileTweet-actionCountForPresentation::text') \
            .extract()

        post_image = account.css('.avatar::attr("src")').extract_first()

        post_tags = tw.css('.tweet-text a.twitter-hashtag b::text') \
            .extract()

        post_tags.append('twitter')

        if tw.css('.context .js-retweet-text a').extract_first():
            tweet_data["action"] = "retweet"
        elif tw.css('.context .Icon--heartBadge'):
            tweet_data["action"] = "like"
            tweet_data["who"] = tw.css('.context .tweet-context::text')

        post = {
            "id": md5(post_url).hexdigest(),
            "description": post_description,
            "type": "twitter-post",
            "authorName": account.css('.FullNameGroup .fullname::text')
            .extract_first(),
            "authorPicture": account.css('.avatar::attr("src")')
            .extract_first(),
            "source": "twitter.com",
            "link": post_url,
            "location": "worlwide",
            "timestamp": post_date,
            "picture": post_image if post_image else DEFAULT_IMG,
            "data": dumps(tweet_data),
            "category": post_tags
        }

        return post

    def get_tweet_description(self, node):
        html = node.css('.tweet-text').extract_first()

        for link in node.css('.tweet-text a'):
            ltype = link.css('::attr("class")').extract()[0].split()
            lhtml = link.extract()
            try:
                if 'twitter-hashtag' in ltype or 'twitter-cashtag' in ltype:
                    first = link.css('b::text').extract_first()
                    hashtag = first if first else link.css('b strong::text').extract_first()
                    html = html.replace(lhtml, '#'+hashtag)
                elif 'twitter-timeline-link' in ltype:
                    html = html.replace(lhtml,
                                        link.css('::attr("data-expanded-url")')
                                        .extract_first())
            except TypeError:
                continue

        ret = Selector(text=html, type="html") \
            .css("::text") \
            .extract_first()

        return ret
