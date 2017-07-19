# -*- coding: utf-8 -*-
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import grequests as req
from json import dumps
from scrapy.pipelines.images import ImagesPipeline
from scrapy.exceptions import DropItem
import scrapy


class MediaPipeline(object):
    def process_item(self, item, spider):
        item['data'] = dumps(item['data'])
        url = 'http://127.0.0.1:5000/api/media/post?fid=%s' % (item['_fid'], )

        del item['_fid']
        del item['image_urls']

        data = dumps(item)
        p = req.post(url, data=data,
                     headers={'Content-type': 'application/json'})
        req.map([p])
        return item


class MediaImagesPipeline(ImagesPipeline):
    def get_media_requests(self, item, info):
        for image_url in item['image_urls']:
            yield scrapy.Request(image_url)

    def item_completed(self, results, item, info):
        image_paths = [x['path'] for ok, x in results if ok]
        if not image_paths:
            raise DropItem("Item contains no images")

        item['data']['_cache'] = image_paths
        return item
