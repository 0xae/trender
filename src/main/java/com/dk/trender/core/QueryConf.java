package com.dk.trender.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class QueryConf {
	private String q;
	private List<String> fq = new ArrayList<>();	
	private List<String> types = new ArrayList<>();
	private String sort="timestamp desc";
	private int limit=5;
	private int start=0;
	
	public QueryConf setTypes(List<String> types) {
		this.types = types;
		return this;
	}
	
	public List<String> getTypes() {
		return types;
	}
	
	public int getStart() {
		return start;
	}

	public QueryConf setStart(int start) {
		this.start = start;
		return this;
	}

	public String getSort() {
		return sort;
	}

	public QueryConf setSort(String sort) {
		this.sort = sort;
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public QueryConf setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public String getQ() {
		return q;
	}

	public QueryConf setQ(String q) {
		this.q = q;
		return this;
	}

	public List<String> getFq() {
		return fq;
	}

	public QueryConf setFq(List<String> fq) {
		this.fq = fq;
		return this;
	}

	public static QueryConf parse(String buf) {
		try {
			return new ObjectMapper()
					   .readValue(buf, QueryConf.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper()
					   .writeValueAsString(this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}
}