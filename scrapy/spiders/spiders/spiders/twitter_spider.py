# -*- coding: utf-8 -*-
import scrapy
from hashlib import md5
from json import dumps
from datetime import datetime
import grequests as greq

INDEX_NAME = 't:twitter'
NO_PICTURE = 'nopicture.png'


class TwitterSpider(scrapy.Spider):
    name = 'twitter_posts'

    def start_requests(self):
        # data = r.json()
        # for item in data:
        start_links = [
            # 'https://twitter.com/XHNews',
            # 'https://twitter.com/ftrain',
            # 'https://twitter.com/_gamemix_',
            # 'https://twitter.com/bobbyclee',
            # 'https://twitter.com/lopp',
            # 'https://twitter.com/AyrtonGsZ',
            # 'https://twitter.com/search?q=bitcoin&src=typd',
            # 'https://twitter.com/search?q=cryptocurrency&src=typd',
            # 'https://twitter.com/search?q=news&src=typd',
            'https://twitter.com/search?f=news&vertical=default&q=cryptocurrency&src=typd'
        ]

        for url in start_links:
            r = scrapy.Request(url=url, callback=self.parse)
            yield r

    def parse(self, response):
        tweets = response.css('.stream-items li.stream-item')
        for tw in tweets:
            post_req = self._domToPost(tw)
            self._createPost(post_req)
            yield post_req

    def _createPost(self, post_req):
        url = 'http://127.0.0.1:5000/api/post/new'
        data = dumps(post_req)
        p = greq.post(url, data=data,
             headers={'Content-type': 'application/json'})
        greq.map([p])

    def _domToPost(self, tw):
        account = tw.css('.content a.account-group')[0]

        author = {
            "title": account.css('.FullNameGroup .fullname::text')
            .extract_first(),
            "link": 'https://twitter.com' + account
            .css('::attr("href")')
            .extract_first(),
            "username": 'tw/' + account.css('::attr("data-user-id")')
            .extract_first(),
            "picture": account.css('.avatar::attr("src")').extract_first()
        }

        post_images = tw.css('.content .AdaptiveMediaOuterContainer ' +
                             'img::attr("src")') \
            .extract()

        post_videos = tw.css('.content .AdaptiveMediaOuterContainer ' +
                             '.PlayableMedia-player::attr("style")') \
            .re(r'\((.*)\)')

        post_url = 'https://twitter.com' + \
            tw.css('.tweet::attr("data-permalink-path")') \
            .extract_first()

        post_description = tw.css('.tweet-text') \
            .extract_first()

        ts = tw.css('.tweet-timestamp ' +
                    'span._timestamp::attr("data-time-ms")') \
            .extract_first()

        post_date = datetime.utcfromtimestamp(long(ts)/1000.0) \
            .strftime('%Y-%m-%dT%H:%M:%S')

        timming = tw.css('.tweet-timestamp::attr("title")') \
            .extract_first()

        tweet_data = {
            "who": author["title"],
            "action": "tweet",
            "replies": 0,
            "retweets": 0,
            "love": 0,
            "images": post_images,
            "videos": post_videos
        }

        if tw.css('.context .js-retweet-text a').extract_first():
            tweet_data["action"] = "retweet"
        elif tw.css('.context .Icon--heartBadge'):
            tweet_data["action"] = "like"
            tweet_data["who"] = tw.css('.context .tweet-context::text')

        post = {
            "description": post_description,
            "blob": dumps(tweet_data),
            "postLink": {
                "viewLink": post_url,
                "commentLink": post_url,
            },
            "postReaction": {},
            "timestamp": post_date,
            "timming": timming,
            "type": "tweet",
            "picture": author["picture"],
            "source": "https://twitter.com",
            "ref": md5(post_url).hexdigest(),
        }

        post_req = {
            "post": post,
            "profile": author,
            "listing": {
                "name": INDEX_NAME
            }
        }

        return post_req

