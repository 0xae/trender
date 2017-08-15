# -*- coding: utf-8 -*-
import scrapy
from hashlib import md5
from json import dumps
from ..trender import create_post, format_date
from datetime import datetime
from urllib import quote_plus


class BBCSpider(scrapy.Spider):
    name = 'bbc_posts'

    def start_requests(self):
        start_urls = [
            'http://www.bbc.com/'
        ]

        for link in start_urls:
            r = scrapy.Request(url=link, callback=self.parse)
            r.meta['link'] = link
            yield r

    def parse(self, response):
        articles = response.css('section.module div.module__content ' +
                                'ul li.media-list__item div.media')
        queue = []
        for art in articles:
            try:
                post = self.parse_article(art, response)
                queue.append(post)
                yield post
            except AttributeError:
                continue

        dbg = "bbc/%s" % quote_plus(self.topic if self.topic else '')
        create_post(queue, debug=dbg)

    def parse_article(self, node, response):
        link = response.meta['link']

        post_image = node.css('div.media__image img::attr("src")') \
            .extract_first() \
            .strip()

        post_title = node.css('div.media__content h3 a::text') \
            .extract_first() \
            .strip()

        post_description = node.css('div.media__content p::text') \
            .extract_first() \
            .strip()

        post_url = node.css('div.media__content ' +
                            'a.media__link::attr("href")') \
            .extract_first()

        post_category_url = node.css('div.media__content ' +
                                     'a.media__tag::attr("href")') \
            .extract_first()

        post_category_url = link + post_category_url

        if post_url.startswith('/'):
            # skip the /
            post_url = link + post_url[1:]

        category = node.css('div.media__content ' +
                            'a.media__tag::text') \
            .extract_first()

        post_tags = []
        post_tags.append('news')
        post_tags.append(category)

        cats = category.split('&')
        if len(cats) > 1:
            post_tags.extend(map(unicode.strip, cats))

        post_date = format_date(datetime.now())

        post_data = {
            "title": post_title,
            "category_url": post_category_url
        }

        post = {
            "id": md5(post_url).hexdigest(),
            "description": post_description,
            "type": "bbc-post",
            "authorName": 'BBC.com',
            "source": "www.bbc.com",
            "link": post_url,
            "location": "worlwide",
            "timestamp": post_date,
            "picture": post_image,
            "data": dumps(post_data),
            "category": post_tags
        }

        return post
