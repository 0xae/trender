# -*- coding: utf-8 -*-
import scrapy
import requests
from hashlib import md5


class BBCSpider(scrapy.Spider):
    name = 'example_posts'

    def start_requests(self):
        r = requests.get('http://127.0.0.1:5000/api/media/index/' + INDEX_NAME)
        data = r.json()
        for item in data:
            r = scrapy.Request(url=item['links'][0], callback=self.parse)
            r.meta['item'] = item
            yield r

    def parse(self, response):
        pass
