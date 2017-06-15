# The Newsfeed Aggregator & Trending topics detector

This is a dropwizard rest app that stores, analyzes and provides a query api to retrieve<br/>
most recent  activities, listing, trendings topics, and all sort of aggregated behavioral information<br/>
happenning on this world.<br/>

This is just half of the code, the other half is the crawler written in javascript/jquery<br/> 
and the frontend app that displays all this information for consumption and configuration,<br/>
more on that later.

The idea is to build something akin to [Trending Topics](https://github.com/datawrangling/trendingtopics), but with a much simpler architecture.<br/>
The **trender** platform crawls, stores and makes use of search technologies and leverages the power<br/> of PostgreSQL to provide you accurate, diverse and descriptive information on realtime or at least near to realtime about what's happening with informations that you feed it, for example data from facebook, twitter, wikipedia, etc, etc.<br/>

## Motivation

    Prove that dropwizard rocks
    Master hibernate
    Build a simple but cool artificial-inteligence to detect trends
    Machine Learning
    Programming
    Having Fun
    Put my Solr and Lucene experience to the top
    Learn more about search/realtime technologies 
    Prove that hibernate can be great if used right ;)
    How far can my thinkpad go ?
    A cool app for myself

## Architecture

### [Trender Apps](https://github.com/0xae/trender-apps)
frontend consumption, manifold of consumers/producers,
crawlers, spiders, ajax, videos, etc

### Trender API (here)
the API allows you to and organize update your data,
and querying the system for very dynamic information.
currently the api is written in java with the dropwizard library
and i love it.

### Trending/Text Analysis/Search/Trending Engine (soon)
search servers, ml & nlp tools. this would be the core of the stuff,
in here we want to use SNLP (Sanford Natural Language Processing [Group]) to detect
trends using sentiment analysis and some kind of word turnover rate. full text search
can be provided via postgres (flexible, fast and convenient) or some other specialized
vendor (lucene, elastic, sphinx).
the reason we need a full-text search offering it's because it doenst make sense
to detect good/bad trends if your searching abilities is poor.
so well the search will have some support for Semantic matching, as it's way cool.

that's it

## Tasks

- [ ] Media content (music, videos, picture, links, etc) API 
- [ ] Trending algorithm and API
- [ ] Natural Language Processing
- [ ] Searching
- [ ] Full Test Coverage
- [ ] Crawling API
- [ ] Dropwizard Metrics ?
- [ ] Rich Descriptive and Analytical API
- [ ] Fix posts timestamp (ajust and better test the TimeParser class)

## Ideas
    
### trending-topics media 
    use crawlers to get media content
### software-engineering
    well, we enjoy the luxury of things happening all the time (crawling activity)
    what if we used that to implement some kind of production testing
    for higher quality

### storage
* [Cockroach DB for storing distributed data](https://cockroachdb-getting-started.glitch.me/#/cluster/all/overview)
    
## Links
### Text Processing
[The Stanford NLP Group](https://nlp.stanford.edu/)

[Learning words (google word2vec)](https://opensource.googleblog.com/2013/08/learning-meaning-behind-words.html)

[google word2vec](https://code.google.com/archive/p/word2vec/)

### Machine Learning
[CS 229 Machine Learning](http://cs229.stanford.edu/projects2013.html)

[Exploring LSTMS](http://blog.echen.me/2017/05/30/exploring-lstms/?imm_mid=0f2ce7&cmp=em-data-na-na-newsltr_20170614)

[RNN Effectiveness](http://karpathy.github.io/2015/05/21/rnn-effectiveness/)

### General Stuff    
[Reddit Atlas](https://www.reddit.com/r/RedditAtlas/)

[Project Atlas](https://draemm.li/various/place-atlas/)

[Generating Fantasy Maps](http://mewo2.com/notes/terrain/)

[Scrappy fast crawler and spider](https://scrapy.org/)

[Augur Prediction](https://augur.net/)




