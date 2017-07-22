# -*- coding: utf-8 -*-
import scrapy
import time 
from hashlib import md5
from json import dumps
import grequests as req
# https://steemit.com/@ayrton


# Default image for pictures withou one
DEFAULT_IMG = '<uk>'


class PostSpider(scrapy.Spider):
    name = 'steemit_posts'

    def start_requests(self):
        url = "https://steemit.com/trending"
        yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        articles = response.css('article')

        for art in articles:
            image_node = art.css('.PostSummary__image::attr("style")') \
                        .re(r'\((.*)\)')
            image = image_node[0] if image_node else DEFAULT_IMG

            post_url = art.css(
                    '.PostSummary__header h3.entry-title a::attr("href")'
                   )[1].extract()

            post_url = 'https://steemit.com' + art.css(
                    '.PostSummary__header h3.entry-title a::attr("href")'
                   )[1].extract()

            post_description = art.css(
                    '.PostSummary__header h3.entry-title a::text'
                    )[1].extract()

            post_time = art.css(
                    '.PostSummary__time_author_category ' +
                    '.show-for-medium .vcard a ' +
                    'span::attr("title")').extract_first()

            post_author = art.css(
                            '.PostSummary__time_author_category ' +
                            ' .author a strong::text').extract_first()

            post_author_link = art.css(
                            '.PostSummary__time_author_category ' +
                            ' .author a::attr("href")').extract_first()

            post_votes = art.css(
                            '.PostSummary__footer ' +
                            '.VotesAndComments__votes::text').extract_first()

            post_comments = art.css(
                            '.PostSummary__footer ' +
                            '.VotesAndComments__comments a::text').extract()

            post = {
                "description": post_description,
                "postLink": {
                    "viewLink": post_url,
                    "commentLink": post_url,
                    "shareLink": post_url
                },
                "postReaction":
                {"countLikes": int(post_votes if post_votes.strip() else 0)},
                "timestamp": int(time.mktime(
                    time.strptime(post_time, '%m/%d/%Y, %H:%M:%S %p'))),
                "timming": post_time,
                "type": "post",
                "picture": image,
                "facebookId": md5(post_url).hexdigest(),
            }

            post_req = {
                "post": post,
                "profile": {
                    "title": post_author,
                    "link": 'https://steemit.com' + post_author_link,
                    "username": post_author
                }
            }

            create_post(post_req)
            yield post_req


def create_post(post):
    url = 'http://127.0.0.1:5000/api/post/new'
    data = dumps(post)
    p = req.post(url, data=data,
            headers={'Content-type': 'application/json'})

    return req.map([p])
