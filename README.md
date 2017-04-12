# The Newsfeed Aggregator & Analyzer

This is a dropwizard rest app that stores, analyzes and provides a query api to retrieve<br/>
most recent  activities, listing, topic trendings, and all sort of behavioral information<br/>
happenning on my facebook.<br/>

This is just half of the code, the other half is the crawler written in javascript/jquery<br/> 
and the frontend app that displays all this information for consumption and configuration,<br/>
more on that later.

The idea is to built something akin to [Trending Topics](https://github.com/datawrangling/trendingtopics), but with a much simpler architecture.<br/>
The **trender** platform crawls, stores and makes use of search technologies and leverages the power<br/> of PostgreSQL to provide you accurate, diverse and descriptive information on realtime or at least near to realtime about what's happening on facebook.<br/> 