package com.dk.trender.core.groups;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.response.QueryResponse;

import com.dk.trender.core.QueryConf;
import com.dk.trender.core.ZGroup;
import com.dk.trender.core.ZPost;

public class NewsGroup implements PostGroup {
	@Override
	public List<ZGroup> process(Map<String, List<ZPost>> types) {
//		term = "bitcoin";
//		minPerGroup = 5;
//		results = searchForTerm(term);
//		foreach (results.facet.fields.category as cat) {
//			if (cat['count'] < minPerGroup) {	
//			}
//		}
		return null;
	}

	private Map<String, List<ZPost>> groupByType(List<String> types, QueryConf conf) {		
//		Map<String, QueryResponse> resp =
//			types.parallelStream()
//			.collect(Collectors.<String, String, QueryResponse>toMap(
//				type -> type,
//				type -> search(conf,type))
//			);
		return null;
	}

//	public S streamCollection(ZChannel chan, ZCollection col) {
//		resp = search(chan, col, conf);
//		category = resp.getFacetFields().category;
//		groups = [main];
//		for (Category cat : category) {
//			if (cat.getCount() == 1) {
//				// this cat should somehow be added to the main group
//			} else if (cat.getCount() > 1) {
//				groups.add(new ZGroup(cat));
//			}
//		}
//	}
}
