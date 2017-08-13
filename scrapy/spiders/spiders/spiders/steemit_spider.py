# -*- coding: utf-8 -*-
import scrapy
from dateutil import parser
from hashlib import md5
from json import dumps
from random import randint
from ..trender import format_date, create_post
from urllib import quote_plus

# Default image for pictures withou one
DEFAULT_IMG = '<uk>'


class SteemitSpider(scrapy.Spider):
    name = 'steemit_posts'

    def start_requests(self):
        url = "https://steemit.com/created"
        yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        articles = response.css('article')
        queue = []

        for art in articles:
            image_node = art.css('.PostSummary__image::attr("style")') \
                        .re(r'\((.*)\)')
            image = image_node[0] if image_node else DEFAULT_IMG

            try:
                post_url = art.css(
                        '.PostSummary__header h3.entry-title a::attr("href")'
                       )[1].extract()
            except IndexError:
                continue

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

            post_tags = art.css('.PostSummary__time_author_category ' +
                                '.vcard strong a::text').extract()

            post_tags.append('steemit')

            post_comments = art.css(
                            '.PostSummary__footer ' +
                            '.VotesAndComments__comments a::text'
                            ).extract_first()

            post_payout = "".join(
                             art.css('.Voting .FormattedAsset span::text')
                            .extract())

            author_reputation = art.css('.author .Reputation::text') \
                .extract_first()

            timestamp = format_date(parser.parse(post_time))

            post_data = {
                "comments": int(post_comments if post_comments.strip() else 0),
                "votes": int(post_votes if post_votes.strip() else 0),
                "payout": post_payout,
                "author_reputation": author_reputation,
                "author_link":  'https://steemit.com'+post_author_link
            }

            post = {
                "id": md5(post_url).hexdigest(),
                "description": post_description,
                "type": "steemit-post",
                "authorName": post_author,
                "source": "steemit.com",
                "link": post_url,
                "location": "worlwide",
                "timestamp": timestamp,
                "picture": image if image else DEFAULT_IMG,
                "data": dumps(post_data),
                "category": post_tags
            }

            queue.append(post)
            yield post

        topic = quote_plus(self.topic if self.topic else '')
        dbg = "steemit/%s" % quote_plus(topic)
        create_post(queue, debug=dbg)

        links = response.css('ul.Topics a::attr("href")').extract()
        next_page = '/tags'
        while next_page == '/tags':
            idx = randint(0, len(links)-1)
            next_page = links[idx]

        if next_page:
            yield response \
                .follow('https://steemit.com' + next_page, self.parse)
