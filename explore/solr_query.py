#!/usr/bin/env python2.7
import requests

URL='http://127.0.0.1:8983/solr/trender/select?q=bitcoin&facet=true&facet.field=category&wt=json&facet.field=type'

def get_facets(json):
    facets=json['facet_counts']
    f=facets['facet_fields']['category']

    for x in range(0, len(f)/2, 2):
        yield (f[x], f[x+1])


def print_facets(topic):
    print "=== %s ===" % (topic, )
    url="""http://127.0.0.1:8983/solr/trender/select?q=%s&facet=true&facet.field=category&wt=json""" % (
                topic,
            )
    resp=requests.get(url)
    json=resp.json()
    facets=get_facets(json)

    for f in facets:
        print f[0], f[1]


if __name__ == '__main__':
    # print_facets('bitcoin')
    print_facets('china')

