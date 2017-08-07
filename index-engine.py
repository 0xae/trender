#!/usr/bin/env python
from twisted.internet import task
from twisted.internet import reactor
from urllib import quote_plus
import requests

queue = []


def get_spider_name(t):
    n = ''
    if t == 'youtube-post':
        n = 'youtube_posts'
    elif t == 'twitter-post':
        n = 'twitter_posts'
    elif t == 'bbc-post':
        n = 'bbc_posts'
    elif t == 'steemit-post':
        n = 'steemit_posts'
    else:
        raise ValueError('Unexpected post-type %s' % t)
    return n


def callback():
    r = requests.get('http://127.0.0.1:5000/api/channel')
    data = r.json()

    for item in data:
        for s in item['spiders']:
            spider_name = get_spider_name(s)
            print 'Indexing #%s with %s' % (item['topic'], spider_name)

            r = requests.post('http://localhost:6800/schedule.json',
                data={'project': 'spiders',
                      'spider': spider_name,
                      'topic': quote_plus(item['topic'])})

    print "========"


def loopDone(result):
    print("Loop Done")
    reactor.stop()


def loopFailed(fail):
    print(fail.getBriefTraceback())
    reactor.stop()


loop = task.LoopingCall(callback)

MINUTES = 5
defer = loop.start(60 * MINUTES)
defer.addCallback(loopDone)
defer.addErrback(loopFailed)

reactor.run()
