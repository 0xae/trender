# -*- coding: utf-8 -*-
import scrapy
from dateutil import parser
from hashlib import md5
from json import dumps
import grequests as req
from random import randint

# Default image for pictures withou one
DEFAULT_IMG = '<uk>'


class PostSpider(scrapy.Spider):
    name = 'steemit_posts'

    def start_requests(self):
        url = "https://steemit.com/created"
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

            post_tag = art.css('.PostSummary__time_author_category ' +
                               '.vcard strong a::text').extract_first()

            post_comments = art.css(
                            '.PostSummary__footer ' +
                            '.VotesAndComments__comments a::text').extract()

            date = parser.parse(post_time)
            timestamp = date.strftime('%Y-%m-%dT%H:%M:%S')

            post = {
                "id": md5(post_url).hexdigest(),
                "description": post_description,
                "type": "post",
                "authorName": post_author,
                "source": "steemit.com",
                "link": post_url,
                "location" : "worlwide",
                "timestamp": timestamp,
                "picture": image,
                "data" : postData
                "category": [post_tag]
            }

            self._create_post(post)
            yield post

        links = response.css('ul.Topics a::attr("href")').extract()
        next_page = '/tags'
        while next_page == '/tags':
            idx = randint(0, len(links)-1)
            next_page = links[idx]

        if next_page:
            yield response \
                .follow('https://steemit.com' + next_page, self.parse)

    def _create_post(self, post):
        url = 'http://127.0.0.1:5000/api/post/new'
        data = dumps(post)
        p = req.post(url, data=data,
                headers={'Content-type': 'application/json'})

        return req.map([p])
