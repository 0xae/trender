import scrapy
from json import dumps
from datetime import datetime
import grequests as req
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
        media = map(_get_video, videos)
        media.extend(map(_get_image, images))

        post = response.meta['item']
        saveAll(media, post)

        for obj in media:
            yield obj


def saveAll(objs, post):
    l = []
    for o in objs:
        data = dumps(o)
        url = 'http://127.0.0.1:5000/api/media/post?fid=%s' % (post['fId'],)
        p = req.post(url, data=data,
                     headers={'Content-type': 'application/json'})
        l.append(p)

    return req.map(l)


def _get_video(url):
    # TODO: get video description
    vtype = "youtube-video"
    obj = {
        "description": "<no>",
        "type": vtype,
        "data": dumps(dict(url=url)),
        "mediaRef": md5(url).hexdigest(),
        "postId": 0,
        "title": "youtube-video"
    }

    return obj


def _get_image(url):
    # TODO: get image description
    obj = {
        "description": "<no>",
        "type": "image",
        "title": "image",
        "data": dumps(dict(url=url)),
        "mediaRef": md5(url).hexdigest(),
        "postId": 0
    }

    return obj
