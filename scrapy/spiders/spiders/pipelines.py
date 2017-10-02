# -*- coding: utf-8 -*-
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

import grequests as req
from json import dumps
from scrapy.pipelines.images import ImagesPipeline
from scrapy.exceptions import DropItem
import scrapy


class MediaImagesPipeline(ImagesPipeline):
    def get_media_requests(self, item, info):
        if item.get('picture'):
            yield scrapy.Request(item.get('picture'))

    def item_completed(self, results, item, info):
        image_paths = [x['path'] for ok, x in results if ok]
        if not image_paths:
            raise DropItem("Item contains no images")

        url = 'http://127.0.0.1:5000/api/post/media/%s' % item['id']
        data = dumps(image_paths)
        p = req.post(url, data=data,
                     headers={'Content-type': 'application/json'})
        req.map([p])
        return item


class MediaPipeline(object):
    def process_item(self, item, spider):
        return item
