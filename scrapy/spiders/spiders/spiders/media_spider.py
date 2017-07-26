# -*- coding: utf-8 -*-
import scrapy
import requests
from hashlib import md5


class MediaSpider(scrapy.Spider):
    name = 'steemit_media'

    def start_requests(self):
        r = requests.get('http://127.0.0.1:5000/api/media/index')
        data = r.json()
        for item in data:
            r = scrapy.Request(url=item['links'][0], callback=self.parse)
            r.meta['item'] = item
            yield r

    def parse(self, response):
        videos = response.css('article .videoWrapper::attr("style")') \
                         .re(r'\((.*)\)')
        images = response.css('article div img::attr("src")').extract()
        media = map(self._get_video, videos)
        media.extend(map(self._get_image, images))
        post = response.meta['item']

        for obj in media:
            obj["_fid"] = post['fId']
            yield obj

    def _get_video(self, url):
        # TODO: get video description
        vtype = "youtube-video"
        data = {"url": url}
        obj = {
            "description": "<no>",
            "type": vtype,
            "data": data,
            "source": "https://steemit.com",
            "mediaRef": md5(url).hexdigest(),
            "postId": 0,
            "title": "youtube-video",
            "image_urls": [url],
        }
        return obj

    def _get_image(self, rawurl):
        # TODO: get image description
        sep = rawurl.find('0x0')
        url = rawurl[sep+4:] if sep > -1 else rawurl
        data = {"url": url}
        obj = {
            "description": "<no>",
            "type": "image",
            "title": "image",
            "source": "https://steemit.com",
            "data": data,
            "mediaRef": md5(url).hexdigest(),
            "image_urls": [url]
        }
        return obj
