# -*- coding: utf-8 -*-
import scrapy
from hashlib import md5
from json import dumps
from dateparser import parse as parse_date
from datetime import datetime
from ..trender import create_post, format_date


class YoutubeSpider(scrapy.Spider):
    name = 'youtube_posts'

    def start_requests(self):
        start_links = [
            'https://www.youtube.com/results?search_query=ethereum',
            'https://www.youtube.com/results?search_query=bitcoin',
            'https://www.youtube.com/results?search_query=china+news',
            'https://www.youtube.com/results?search_query=asia+news',
            'https://www.youtube.com/results?q=stock+market&sp=EgIIBFAU',
            'https://www.youtube.com/results?q=rick+and+morty&sp=EgIIBFAU'
        ]

        for url in start_links:
            r = scrapy.Request(url=url, callback=self.parse)
            yield r

        # r = requests.get('http://127.0.0.1:5000/api/media/index/' + INDEX_NAME)
        # data = r.json()
        # for item in data:
        #     r = scrapy.Request(url=item['links'][0], callback=self.parse)
        #     r.meta['item'] = item
        #     yield r

    def parse(self, response):
        videos = response.css('#results ol.item-section li div.yt-lockup')
        request = map(self.get_youtube_post, videos)
        create_post(request)

    def get_youtube_post(self, node):
        video_id = node.css('::attr("data-context-item-id")') \
            .extract_first()

        video_description = node.css('.yt-lockup-content ' +
                                     'h3.yt-lockup-title a::text') \
            .extract_first()

        video_link = 'https://youtube.com' + \
            node.css('.yt-lockup-title a::attr("href")') \
            .extract_first()

        video_author = node.css(".yt-lockup-content " +
                                ".yt-lockup-byline a::text") \
            .extract_first()

        channel_id = node.css(".yt-lockup-content " +
                             ".yt-lockup-byline a::attr('data-ytid')") \
            .extract_first()

        video_image = node.css(".yt-lockup-thumbnail img::attr('src')") \
            .extract_first()

        human_date = node.css('.yt-lockup-content ' +
                              '.yt-lockup-meta ul li::text') \
            .extract_first()

        date = parse_date(human_date)
        video_date = format_date(date if date else datetime.now())

        try:
            views = node.css('.yt-lockup-content ' +
                             '.yt-lockup-meta ul li::text')[1] \
                .extract() \
                .replace(',', '') \
                .strip()

            views = str(views)
            views = int(views[0:views.index(' ')])
        except (IndexError, ValueError):
            views = 0

        video_data = {
            "likes": 0,
            "views": views,
            "video_id": video_id,
            "channel_id": channel_id
        }

        post = {
            "id": md5(video_link).hexdigest(),
            "description": video_description,
            "type": "youtube-post",
            "authorName": video_author,
            "source": "www.youtube.com",
            "link": video_link,
            "location": "worlwide",
            "timestamp": video_date,
            "picture": video_image,
            "data": dumps(video_data),
            "category": ['youtube', 'video']
        }

        return post
