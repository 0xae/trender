package com.dk.trender.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ZSearch {
	private String q;
	private List<String> fq = new ArrayList<>();	
	private List<String> types = new ArrayList<>();
	private String sort="timestamp desc";
	private int limit=5;
	private int start=0;
			
	public ZSearch() {
	}
	
	public ZSearch(MultivaluedMap<String, String> params) {		
	}

	public ZSearch setTypes(List<String> types) {
		this.types = types;
		return this;
	}

	public List<String> getTypes() {
		return types;
	}

	public int getStart() {
		return start;
	}

	public ZSearch setStart(int start) {
		this.start = start;
		return this;
	}

	public String getSort() {
		return sort;
	}
	
	public ZSearch setSort(String sort) {
		this.sort = sort;
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public ZSearch setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public String getQ() {
		return q;
	}

	public ZSearch setQ(String q) {
		this.q = q;
		return this;
	}

	public List<String> getFq() {
		return fq;
	}

	public ZSearch setFq(List<String> fq) {
		this.fq = fq;
		return this;
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
	
	public static ZSearch parse(String buf) {
		try {
			return new ObjectMapper()
					   .readValue(buf, ZSearch.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}