package com.dk.trender.exceptions;

import java.util.List;

public class BadRequest extends RuntimeException {
	private List<String> errors;

	public BadRequest(List<String> e) {
		this.errors = e;
	}

	public List<String> errors() {
		return errors;
	}
}
