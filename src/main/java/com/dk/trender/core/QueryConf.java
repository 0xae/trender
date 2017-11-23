package com.dk.trender.core;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class QueryConf {
	private String q;
	private List<String> fq = Collections.EMPTY_LIST;
	private int limit=10;
	private String sort="timestamp desc";
	private int start=0;
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public List<String> getFq() {
		return fq;
	}

	public void setFq(List<String> fq) {
		this.fq = fq;
	}

	public static QueryConf parse(String buf) {
		try {
			// XXX: should we move mapper to a higher level (private static) ?
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(buf, QueryConf.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}