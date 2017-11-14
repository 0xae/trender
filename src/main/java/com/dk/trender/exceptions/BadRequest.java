package com.dk.trender.exceptions;

import java.util.List;

public class BadRequest extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private List<String> errors;
	private int status=0;

	public BadRequest(int status, List<String> e) {
		this.status = status;
		this.errors = e;
	}
	
	public BadRequest(List<String> e) {
		this.errors = e;
	}

	public List<String> errors() {
		return errors;
	}
	
	public int status() {
		return status;
	}
}
