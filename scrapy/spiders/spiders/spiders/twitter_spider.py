# -*- coding: utf-8 -*-
import scrapy
import requests
from hashlib import md5
from json import dumps

INDEX_NAME = 't:twitter'
NO_PICTURE = ''


class TwitterSpider(scrapy.Spider):
    name = 'twitter_posts'

    def start_requests(self):
        # r = requests.get('http://127.0.0.1:5000/api/media/index/' + INDEX_NAME)
        # data = r.json()
        # for item in data:
        start_links = [
            'https://twitter.com/XHNews',
            'https://twitter.com/ftrain',
            'https://twitter.com/_gamemix_',
            'https://twitter.com/bobbyclee',
            'https://twitter.com/lopp',
            'https://twitter.com/AyrtonGsZ'
        ]

        for url in start_links:
            r = scrapy.Request(url=url, callback=self.parse)
            yield r

    def parse(self, response):
        tweets = response.css('.stream-items li')
        for tw in tweets:
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

            post_image = tw.css('.content  .AdaptiveMediaOuterContainer img')\
                          .extract()

            # post = {
            #     "description": post_description,
            #     "postLink": {
            #         "viewLink": post_url,
            #         "commentLink": post_url,
            #         "shareLink": post_url
            #     },
            #     "postReaction":
            #     {"countLikes": int(post_votes if post_votes.strip() else 0)},
            #     "timestamp": timestamp,
            #     "timming": post_time,
            #     "type": "post",
            #     "picture": post_image[0] if post_image else NO_PICTURE,
            #     "source": "https://steemit.com",
            #     "facebookId": md5(post_url).hexdigest(),
            # }

            # # post_req = {
            #     "post": post,
            #     "profile": account,
            #     "listing": {
            #         "name": INDEX_NAME
            #     }
            # }

            print dumps(author, indent=4)
