package com.dk.trender.core;

import java.util.List;

public class ZFeed {
	private String name;
	private List<ZCollection> privateCols;
	private List<ZCollection> publicCols;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ZCollection> getPrivateCols() {
		return privateCols;
	}

	public void setPrivateCols(List<ZCollection> privateCols) {
		this.privateCols = privateCols;
	}

	public List<ZCollection> getPublicCols() {
		return publicCols;
	}

	public void setPublicCols(List<ZCollection> publicCols) {
		this.publicCols = publicCols;
	}	
}
