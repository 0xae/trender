from SimpleHTTPServer import SimpleHTTPRequestHandler
from BaseHTTPServer import HTTPServer
import sys

s = HTTPServer(
    ('0.0.0.0', 8000),
    SimpleHTTPRequestHandler
)

s.serve_forever()
