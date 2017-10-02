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
        raise ValueError('Unexpected post-type "%s"' % t)


def get_conf():
    if len(sys.argv) == 3:
        host = sys.argv[1]
        interval = int(sys.argv[2])
    else:
        print "Usage: index-engine.py <host> <interval>"
        sys.exit(1)

    return (host, interval)


def callback():
    """ send the indexing work to scrapy  """
    host, _ = get_conf()
    r = requests.get('http://%s:5000/api/timeline?state=temp' % host)
    data = r.json()

    for item in data:
        postType = item['postTypes'].split(',')
        for s in postType:
            spider_name = get_spider_name(s)
            print 'indexing: %s/%s ' % (spider_name, item['topic'])

            r = requests.post('http://%s:6800/schedule.json' % host,
                              data={'project': 'spiders',
                                    'spider': spider_name,
                                    'topic': quote_plus(item['topic'])})

    print "done indexing\n=========\n"


def loop_done(result):
    print("Loop Done: ", result)
    reactor.stop()


def loop_failed(fail):
    print(fail.getBriefTraceback())
    reactor.stop()


if __name__ == "__main__":
    _, interval = get_conf()
    print "Indexing interval: %dmn" % interval

    loop = task.LoopingCall(callback)
    defer = loop.start(60 * interval)
    defer.addCallback(loop_done)
    defer.addErrback(loop_failed)

    reactor.run()
