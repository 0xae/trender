package com.dk.trender.core;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.validation.OneOf;
import org.joda.time.format.DateTimeFormat;


/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:42
 */
@Entity
@Table(name="z_post")
@NamedQueries({
	@NamedQuery(
	    name = "post.findAll",
	    query = "from Post"
	),
	@NamedQuery(
	    name = "post.findByFacebook",
	    query = "from Post where facebook_id = :facebookId"
	)
})
public class Post {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

	@Column(name="description")
	private String description;

	@NotEmpty
	@Column(name="profile_id", nullable=false, updatable=false)
	private long profileId;
	
	@Column(name="cover_html", updatable=false)
	private String coverHtml;

	@Column(name="time", updatable=false)
	private DateTime timestamp;

	@NotEmpty
	@Column(name="facebook_id", unique=true, updatable=false)
	private String facebookId;

	@Column(name="listing_id", updatable=false)
	private long listingId;

	@NotEmpty
	@OneOf({"event", "post"})
	@Column(name="type", nullable=false, updatable=false)
	private String type;

	@Valid
	@NotNull
	@Embedded
	private PostReaction postReaction;

	@Valid
	@NotNull
	@Embedded
	private PostLink postLink;

	public Post() {
		// TODO
	}
	
	@JsonProperty
	public void setCoverHtml(String coverHtml) {
		this.coverHtml = coverHtml;
	}
	
	@JsonProperty
	public String getCoverHtml() {
		return coverHtml;
	}

	@JsonProperty
	public long getProfileId() {
		return profileId;
	}

	@JsonProperty
	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	@JsonProperty
	public PostLink getPostLink() {
		return postLink;
	}

	@JsonProperty
	public void setPostLink(PostLink postLink) {
		this.postLink = postLink;
	}

	@JsonProperty
	public long getListingId() {
		return listingId;
	}

	@JsonProperty
	public String getTimestampFmt() {
		return DateTimeFormat.forPattern("YYY-MM-d HH:mm:ss")
		.print(timestamp);
	}

	@JsonProperty
	public void setListingId(long listingId) {
		this.listingId = listingId;
	}

	@JsonProperty
	public String getFacebookId() {
		return facebookId;
	}

	@JsonProperty
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@JsonProperty
	public void setPostReaction(PostReaction postReaction) {
		this.postReaction = postReaction;
	}
	
	@JsonProperty
	public PostReaction getPostReaction() {
		return postReaction;
	}
	
	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}

	@JsonProperty
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty
	public DateTime getTimestamp() {
		return timestamp;
	}

	@JsonProperty
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty
	public String getType() {
		return type;
	}

	@JsonProperty
	public void setType(String type) {
		this.type = type;
	}
}
