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

### ML/Search/Trending Engine (soon)
search servers, ml & nlp tools. this would be the core of the stuff,
in here we want to use snlp (Sanford Natural Language Processing [Group]) to detect
trends via use of sentiment analysis and some kind of word turnover rate. full text search
can be provided via postgres (flexible, fast and convenient) or some other specialized
vendor (lucene, elastic, sphinx).
the reason we need a full-text search offering it's because it doenst make sense
to detect good/bad trends if your searching abilities is poor.
so well the search will have some support for Semantic matching, as it way cool.

that's it
