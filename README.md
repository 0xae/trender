# Trender - an exquisite newsfeed

This is an app that aggregates the most recent and popular tweets, the most interesting videos from youtube,
what's happening in the stock and crypto markets, the most recent news, activities and every relevant information happening in realtime on the internet. we also feature a live tv that aggregates videos and livestreams of anime, tv shows, news (again), music, films, and also funny, cool and entertaining cartoons. 
Trender aggregates data from google, facebook, youtube, twitter, wikipedia, blogs, websites, communities, forums, etc and from this abundance of information and media it builds the ultimate media-internet-entertainment-news network.

Simply put: trender aims to be the frontpage of my internet.

The idea is to build something akin to [Trending Topics](https://github.com/datawrangling/trendingtopics), but with a much richer and faster and cheaper architecture, that runs fine on localhost without aid of the fatso hadoop.<br/>
The **trender** platform crawls, stores and makes use of search technologies, spiders, databases and artificial inteligence to produce ~accurate, diverse and descriptive information on realtime or at least near to realtime about what's happening in the world. it does so by collecting, processing and aggregating data from the various sources.<br/>

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

### Crypto
* [namecoin](https://namecoin.org)

### News
* [news24](https://www.news24.com)
* [herald.co.zw](http://www.herald.co.zw)
* [4news](https://www.channel4.com)

### infraestructure
* [list of crawlers](https://github.com/BruceDone/awesome-crawler)
* [gigablast](https://github.com/gigablast/open-source-search-engine)
* [erlang ebot](http://www.redaelli.org/matteo-blog/projects/ebot/)
* [Scrapy](https://docs.scrapy.org/en/latest/topics)
* [grequests](https://pypi.python.org/pypi/grequests)
* [Command-line JSON processor](https://github.com/stedolan/jq)
* [awesome-streaming](https://github.com/manuzhang/awesome-streaming)
* [jet.hazelcast](https://jet.hazelcast.org/introduction/)
* [@rwalk straw](https://github.com/rwalk/straw)
* [@rwalk blog post on straw](http://blog.ryanwalker.us/2015/11/building-streaming-search-platform.html)
* [luwak](https://github.com/flaxsearch/luwak)
* [real-time-full-text-search-with-luwak-and-samza](https://www.confluent.io/blog/real-time-full-text-search-with-luwak-and-samza/)
* [scalable web crawlers on Apache Storm](http://stormcrawler.net/index.html)
* [ebot crawler blogpost](http://www.redaelli.org/matteo-blog/projects/ebot/)
* [seleniumhq.org](http://www.seleniumhq.org)
* [htaccess snippets](https://github.com/phanan/htaccess)
* [syntax-highlighter](https://github.com/syntaxhighlighter/)
* [twitter-emoji](https://github.com/twitter/twemoji)


### Public Datasets
* [amazon public datasets](https://aws.amazon.com/public-datasets/)
* [NASA Image Gallery](https://landsat.visibleearth.nasa.gov/)

### Artificial intelligence / Machine Learning
* [yago Knowledge Base](https://www.mpi-inf.mpg.de/departments/databases-and-information-systems/research/yago-naga/yago/)
* [caffe.berkeleyvision(deep learning framework)](http://caffe.berkeleyvision.org)
* [The Stanford NLP Group](https://nlp.stanford.edu/)
* [Stanford CoreNLP](https://github.com/stanfordnlp/CoreNLP)
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
* [OpenAI](https://openai.com/)
* [openai systems](https://openai.com/systems/)
* [amazon mechanical turk](https://www.mturk.com/mturk/welcome)
* [deeplearning.net](http://deeplearning.net)
* [artificial-intelligence-revealed (facebook)](https://code.facebook.com/posts/384869298519962/artificial-intelligence-revealed/)


### Multimedia/Games websites
* [UK Tv Guide](http://www.tvguide.co.uk/)
* [Youtube API](https://www.youtube.com/yt/dev/demos.html#/sentiment)
* [My5](https://www.my5.tv)
* [Webtorrent IO](https://webtorrent.io/intro)
* [DTube](https://steemit.com/video/@heimindanger/introducing-dtube-a-decentralized-video-platform-using-steem-and-ipfs)
* [Awesome russian youtube](https://plus.kinopoisk.ru/)

### Gamming platform
* [darkforestGo](https://github.com/facebookresearch/darkforestGo)
* [Facebook Game Research](https://github.com/facebookresearch/ELF)

### API
* [openweathermap](https://openweathermap.org/city/3374333)
* [thetvdb](https://www.thetvdb.com/)

### Markets
* [Oil Price](http://oilprice.com)
* [blockchain](https://blockchain.info)
* [blockchair](https://blockchair.com)
* [coinmarketcap](https://coinmarketcap.com)
* [bravenewcoin](https://bravenewcoin.com)
* [bitcoin developer guide](https://bitcoin.org/en/developer-guide)
* [cryptocompare](https://www.cryptocompare.com)
* [fork.lol](http://fork.lol)
* [bitcoin cash links](https://github.com/dsmurrell/awesome-bitcoin-cash)
* [thehalvening](http://www.thehalvening.com/#1)


### Blogs
* [Nick Szabo](https://unenumerated.blogspot.com)

### Random Stuff
* [10-print-in-python](https://www.makeartwithpython.com/blog/10-print-in-python/)
* [stephenking.com](http://stephenking.com)
* [Lively PowerfulBird game announcemnt](https://www.reddit.com/r/gaming/comments/7blahv/my_friends_are_making_a_game_where_you_build_your/)
* [Lively PowerfulBird game](https://gfycat.com/LivelyPowerfulBird)
* [twitter.com/Channel4News](https://twitter.com/Channel4News)
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
* [Government Types](https://www.pinterest.com/pin/521432463090231883/)
* [Jp live musics](https://www.showroom-live.com/akb48_asuyoro)
* [rotten tomatoes](https://www.rottentomatoes.com)
* [imdb.com](http://www.imdb.com)


### A Geek amusings
* [geeking.co](http://geeking.co/)
* [nerdist.com](http://nerdist.com/)
* [soundcloud/subwayssounds](https://soundcloud.com/subwayssounds)
* dystopian
* cyberpunk
* serial experiments of lain
* [dystopian novels](https://www.pinterest.com/pin/484559241149258473/)
* [cyverpunky room](http://i.imgur.com/5wHPuYU.jpg)
* [what is dystopia](https://www.pinterest.com/pin/AeekDsqi3kYucqTgTriJlm1dPD8rD68UnrZB83Tf3wZnZ-jPidNNh3A/)
* [funnyjunk.com](https://funnyjunk.com/)
* [lain on kinopoisk](https://plus.kinopoisk.ru/film/321835/)
* [Morpheus Cat](https://funnyjunk.com/funny_pictures/3499636/Morpheus+cat/)
* [OMG Ubuntu on Pinterest](https://www.pinterest.com/pin/527836018817053853/)
* [gratuitous-cyberpunk](http://gratuitous-cyberpunk-sex.tumblr.com/)
* [awesome-gophers](https://github.com/ashleymcnamara/gophers)
* [clojure in detail (chinese)](http://blog.fnil.net/blog/9a8d68348d52bc45c6a1273a272d9f52/)

we're up all night to get lucky 
