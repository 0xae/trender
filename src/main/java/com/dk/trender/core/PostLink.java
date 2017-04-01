package com.dk.trender.core;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
@Access(AccessType.FIELD)
public class PostLink {
	@Column(name="share_link")
	private String shareLink;

	@NotEmpty
	@Column(name="comment_link", nullable=false, updatable=false)
	private String commentLink;

	@NotEmpty
	@Column(name="view_link", nullable=false, updatable=false)
	private String viewLink;

	@JsonProperty
	public String getShareLink() {
		return shareLink;
	}

	@JsonProperty
	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	@JsonProperty
	public String getCommentLink() {
		return commentLink;
	}

	@JsonProperty
	public void setCommentLink(String commentLink) {
		this.commentLink = commentLink;
	}

	@JsonProperty
	public String getViewLink() {
		return viewLink;
	}

	@JsonProperty
	public void setViewLink(String viewLink) {
		this.viewLink = viewLink;
	}
}
