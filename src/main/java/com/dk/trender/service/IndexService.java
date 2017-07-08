package com.dk.trender.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IndexService {
	private Queue<String> queue;

	public IndexService() {
		this.queue = new ConcurrentLinkedQueue<String>();
	}

	public IndexService(Queue<String> queue) {
		this.queue = queue;
	}

	public void addToIndex(List<String> url) {		
		queue.addAll(url);
	}

	public List<String> retrieveIndex() {
		List<String> buf = new LinkedList<>();
		String c;
		int t=0;
		while ((c=queue.poll()) != null && (++t < 10)) {
			buf.add(c);			
		}
		
		// DEBUG purposes
		addToIndex(buf);
		return buf;
	}
	
	public int indexSize() {
		return queue.size();
	}
}
