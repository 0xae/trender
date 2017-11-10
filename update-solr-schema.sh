#!/bin/bash
trender_dir='/opt/trender/solr-6.6.0/server/solr/trender/'
solr_dir='/opt/trender/solr-6.6.0'

echo "installing new schema file"
cp src/main/resources/schema.xml $trender_dir/conf

echo "restarting solr"
$solr_dir/bin/solr restart 

