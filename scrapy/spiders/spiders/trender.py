from json import dumps
import grequests as req


def create_post(post, debug=''):
    url = 'http://127.0.0.1:5000/api/post/new'
    if debug:
        url = url + "?debug=" + debug
    data = dumps(post)
    p = req.post(url, data=data,
                 headers={'Content-type': 'application/json'})

    return req.map([p])


def format_date(date):
    return date.strftime('%Y-%m-%dT%H:%M:%S')
