# -*- coding: utf-8 -*-
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import grequests as req
from json import dumps


class MediaPipeline(object):
    def process_item(self, item, spider):
        d = item['data']
        cache = item['images']
        print '>>> ', cache
        d['_cache'] = cache
        item['data'] = dumps(item['data'])
        url = 'http://127.0.0.1:5000/api/media/post?fid=%s' % (item['_fid'], )
        del item['_fid']
        del item['image_urls']
        del item['images']
        data = dumps(item)
        p = req.post(url, data=data,
                     headers={'Content-type': 'application/json'})
        req.map([p])
        return item
