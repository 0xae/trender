# An exquisite newsfeed

This is a rest app that stores, analyzes and provides a query api to retrieve<br/>
most recent  activities, listing, trendings topics, and all sort of aggregated behavioral information<br/>
happenning.<br/>

The idea is to build something akin to [Trending Topics](https://github.com/datawrangling/trendingtopics), but with a much simpler and faster and cheaper architecture, that runs fine on localhost.<br/>
The **trender** platform crawls, stores and makes use of search technologies and leverages the power<br/> of PostgreSQL to provide you accurate, diverse and descriptive information on realtime or at least near to realtime about what's happening with informations that you feed it, for example data from facebook, twitter, wikipedia, etc, etc.<br/>

This is alpha release and probably wont come to beta seas until Xstmas.

## Motivation

    Dropwizard rocks
    Master hibernate
    Build a simple but cool artificial-inteligence to detect trends
    Programming & Fun
    Put my Solr and Lucene experience to the top
    Learn more about search/realtime technologies 
    How far can my thinkpad go ?
    A cool app for myself

## Architecture

### [Trender Apps](https://github.com/0xae/trender-apps)
Fast, beautifull and interactive apps, featuring videos, pictures, tweets and news
frontend consumption, manifold of consumers/producers,
crawlers, spiders, ajax, videos, etc

### Trender API (here)
the API allows you to and organize update your data,
and querying the system for dynamic information.
currently the api is written in java with the dropwizard library
and i love it.

### Trending/Text Analysis/Search (soon)
search servers, ml & nlp tools. this would be the core of the stuff,
in here we want to use SNLP (Sanford Natural Language Processing [Group]) to detect
trends using sentiment analysis and some kind of word turnover rate. full text search
can be provided via postgres (flexible, fast and convenient) or some other specialized
vendor (lucene, elastic, sphinx).
the reason we need a full-text search offering it's because it doenst make sense
to detect good/bad trends if your searching abilities are poor.
so well the search will have some support for Semantic matching, as it's way cool.

that's it

## Tasks

- [ ] Natural Language Processing
- [ ] Dropwizard Metrics ?
- [ ] Trending API
- [ ] Crawling API
- [ ] Discovery API
- [ ] Searching API
- [ ] Searching infraestructure optimization (solr)
- [X] Scrapy crawling infraestructure

## Ideas
    
### trending-topics media 
    use spiders to get media content
### live-testing
    well, we enjoy the luxury of things happening all the time (crawling activity)
    what if we used that to implement some kind of serve->test ( ~live testing ? )
### P2P network
    bittorrent architecture & checkout tornet
    
## Links
### Storage
* [Cockroach DB for storing distributed data](https://cockroachdb-getting-started.glitch.me/#/cluster/all/overview)
* [Hibernate User Guide](http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#architecture-overview)

### Scrapy & Friends
* [Scrapy](https://docs.scrapy.org/en/latest/topics)
* [grequests](https://pypi.python.org/pypi/grequests)

### Multimedia websites
* [UK Tv Guide](http://www.tvguide.co.uk/)
* [Youtube API](https://www.youtube.com/yt/dev/demos.html#/sentiment)
* [My5](https://www.my5.tv)
* [Webtorrent IO](https://webtorrent.io/intro)
* [DTube](https://steemit.com/video/@heimindanger/introducing-dtube-a-decentralized-video-platform-using-steem-and-ipfs)

### API
* [openweathermap](https://openweathermap.org/city/3374333)

### Text/Data Processing
* [Command-line JSON processor](https://github.com/stedolan/jq)

### real-time streaming search
* [awesome-streaming](https://github.com/manuzhang/awesome-streaming)
* [jet.hazelcast](https://jet.hazelcast.org/introduction/)
* [@rwalk straw](https://github.com/rwalk/straw)
* [@rwalk blog post on straw](http://blog.ryanwalker.us/2015/11/building-streaming-search-platform.html)
* [luwak](https://github.com/flaxsearch/luwak)
* [real-time-full-text-search-with-luwak-and-samza](https://www.confluent.io/blog/real-time-full-text-search-with-luwak-and-samza/)

### Artificial intelligence / Machine Learning
* [The Stanford NLP Group](https://nlp.stanford.edu/)
* [Learning words (google word2vec)](https://opensource.googleblog.com/2013/08/learning-meaning-behind-words.html)
* [google word2vec](https://code.google.com/archive/p/word2vec/)
* [CS 229 Machine Learning](http://cs229.stanford.edu/projects2013.html)
* [Exploring LSTMS](http://blog.echen.me/2017/05/30/exploring-lstms/?imm_mid=0f2ce7&cmp=em-data-na-na-newsltr_20170614)
* [RNN Effectiveness](http://karpathy.github.io/2015/05/21/rnn-effectiveness/)
* [IBM Watson](http://watson-developer-cloud.github.io/java-sdk/)
* [Facebook ai-negociator-bots](/https://code.facebook.com/posts/1686672014972296/deal-or-no-deal-training-ai-bots-to-negotiate/)
* [Quora QMF](https://github.com/quora/qmf)
* [Facebook DeepText](https://code.facebook.com/posts/181565595577955/introducing-deeptext-facebook-s-text-understanding-engine/)
* [Facebook ParlAI](https://code.facebook.com/posts/266433647155520/parlai-a-new-software-platform-for-dialog-research/)

### Random Stuff    
* [Random UK post code generator](https://www.doogal.co.uk/PostcodeGenerator.php)
* [Reddit Atlas](https://www.reddit.com/r/RedditAtlas/)
* [Project Atlas](https://draemm.li/various/place-atlas/)
* [Generating Fantasy Maps](http://mewo2.com/notes/terrain/)
* [Scrappy fast crawler and spider](https://scrapy.org/)
* [Augur Prediction](https://augur.net/)
* [Consensus & Paxos](https://hackernoon.com/how-your-data-is-stored-or-the-laws-of-the-imaginary-greeks-54c569c17a49)
* [kafka & Message Queues](https://hackernoon.com/a-super-quick-comparison-between-kafka-and-message-queues-e69742d855a8)
* [Awesome blog, full of code](https://www.approach0.xyz/tkblog/)
* [math-aware search engine](https://github.com/approach0/search-engine)
* [south korea trending posts on steemit](https://steemit.com/trending/kr)
* [github markdown](https://guides.github.com/features/mastering-markdown/)
* [crushthestreet.com](https://crushthestreet.com)
* [cnblogs.com (chinese)](https://www.cnblogs.com/)
* [庄周梦蝶](http://fnil.net/)
* [erlang article at cnblogs](http://www.cnblogs.com/me-sa/archive/2011/11/13/erlang0014.html)
* [jvm article at cnblogs](http://www.cnblogs.com/-new/p/7244460.html)
* [douban looks awesome](https://www.douban.com/people/killme2008/)

### A Geek amusings
* [geeking.co](http://geeking.co/)
* [nerdist.com](http://nerdist.com/)
* [soundcloud/subwayssounds](https://soundcloud.com/subwayssounds)

we're up all night to get lucky 
