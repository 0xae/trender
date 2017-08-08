#!/usr/bin/env python
from twisted.internet import task
from twisted.internet import reactor
from urllib import quote_plus
import requests
import sys


def get_spider_name(t):
    """ returns the real scrapy spider name """
    if t in ('youtube-post', 'twitter-post',
             'bbc-post', 'steemit-post'):
        return t.replace('-', '_') + 's'
    else:
        raise ValueError('Unexpected post-type %s' % t)


def callback():
    """ send the indexing work to scrapy  """
    r = requests.get('http://127.0.0.1:5000/api/channel')
    data = r.json()

    for item in data:
        for s in item['spiders']:
            spider_name = get_spider_name(s)

            r = requests.post('http://localhost:6800/schedule.json',
                    data={'project': 'spiders',
                          'spider': spider_name,
                          'topic': quote_plus(item['topic'])})


def loopDone(result):
    print("Loop Done")
    reactor.stop()


def loopFailed(fail):
    print(fail.getBriefTraceback())
    reactor.stop()


if __name__ == "__main__":
    minutes = 5
    if len(sys.argv) > 1:
        minutes = int(sys.argv[1])

    print "Indexing interval: %dmn" % minutes

    loop = task.LoopingCall(callback)
    defer = loop.start(60 * minutes)
    defer.addCallback(loopDone)
    defer.addErrback(loopFailed)

    reactor.run()
