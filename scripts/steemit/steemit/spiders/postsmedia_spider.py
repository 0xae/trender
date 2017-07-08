import scrapy
from json import dumps
from datetime import datetime


class PostMediaSpider(scrapy.Spider):
    name = 'steemit_posts_media'

    def start_requests(self):
        urls = []
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        videos = response.css('article .videoWrapper::attr("style")').re(r'\((.*)\)')
        images = response.css('article div img::attr("src")')
        videos = map(videos, _get_video)
        images = map(images, _get_image)
        saveAll(videos)
        saveAll(images)
        pass


def saveAll(objs):
    pass


def _get_video(url):
    # XXX: strftime works on unix systems only
    # TODO: get video description
    vtype = "youtube-video"
    obj = {
        "description": "",
        "type": vtype,
        "time": datetime.now().strftime('%s'),
        "data": dumps(dict(url=url))
    }

    return obj


def _get_image(url):
    # XXX: strftime works on unix systems only
    # TODO: get image description
    obj = {
        "description": "",
        "type": "image",
        "time": datetime.now().strftime('%s'),
        "data": dumps(dict(url=url))
    }

    return obj
