from json import dumps
import grequests as req


def create_post(post):
    url = 'http://127.0.0.1:5000/api/post/new'
    data = dumps(post)
    p = req.post(url, data=data,
                 headers={'Content-type': 'application/json'})

    return req.map([p])


def format_date(date):
    return date.strftime('%Y-%m-%dT%H:%M:%S')
