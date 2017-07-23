package com.dk.trender.core;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.persistence.ForeignKey;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.validation.OneOf;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:42
 */
@Entity
@Table(name="z_post")
@NamedQueries({
	@NamedQuery(
	    name="post.findAll",
	    query="from Post"
	),
	@NamedQuery(
	    name="post.updateLike",
	    query="update Post p set count_likes=:likes where id = :id"
	),
	@NamedQuery(
	    name="post.updatePicture",
	    query="update Post p set picture=:pic where facebook_id = :facebook_id"
	),
	@NamedQuery(
	    name="post.findByFacebook",
	    query="from Post where facebook_id = :facebookId"
	)
})
public class Post {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

	@NotEmpty
	@Column(name="description", nullable=false)
	private String description;

	@NotEmpty
	@Column(name="picture", nullable=false)
	private String picture;

	@NotEmpty
	@Column(name="timming", nullable=false, updatable=false)
	private String timming;

	@Column(name="profile_id", nullable=false, updatable=false)
	private long profileId;

	@Column(name="cover_html", updatable=false)
	private String coverHtml;

	@Column(name="source", updatable=false)
	private String source;	

	@Column(name="time", updatable=false, nullable=false)
	private LocalDateTime timestamp;

	@Column(name="indexed_at", updatable=false)
	private LocalDateTime indexedAt;

	@NotEmpty
	@Column(name="facebook_id", unique=true, updatable=false)
	private String facebookId;

	@Column(name="listing_id", updatable=false)
	private long listingId;

	@Column(name="blob")
	@ColumnTransformer(write="?::jsonb")
	private String blob;	
	
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

	@ManyToOne
	@JoinColumn(name="listing_id", insertable=false, updatable=false, foreignKey=@ForeignKey(name="listing_id_fkey"))
	private Listing listing;

	@ManyToOne
	@JoinColumn(name="profile_id", insertable=false, updatable=false, foreignKey=@ForeignKey(name="profile_id_fkey"))
	private Profile author;

	public Post() {
		// TODO
	}
	
	@JsonProperty
	public void setSource(String source) {
		this.source = source;
	}
	
	@JsonProperty
	public String getSource() {
		return source;
	}

	@JsonProperty
	public void setBlob(String blob) {
		this.blob = blob;
	}

	@JsonProperty
	public String getBlob() {
		return blob;
	}

	@JsonProperty
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	@JsonProperty
	public String getPicture() {
		return picture;
	}	

	@JsonProperty
	public void setListing(Listing listing) {
		this.listing = listing;
	}

	@JsonProperty
	public Listing getListing() {
		return listing;
	}

	@JsonProperty
	public void setAuthor(Profile profile) {
		this.author = profile;
	}

	@JsonProperty
	public Profile getAuthor() {
		return author;
	}

	@JsonProperty
	public void setIndexedAt(LocalDateTime indexedAt) {
		this.indexedAt = indexedAt;
	}

	@JsonProperty
	public LocalDateTime getIndexedAt() {
		return indexedAt;
	}

	@JsonProperty
	public void setTimming(String timming) {
		this.timming = timming;
	}

	@JsonProperty
	public String getTimming() {
		return timming;
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
		return timestamp.toString();
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
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	@JsonProperty
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty
	public void setTimestamp(String timestamp) {
		this.timestamp = new LocalDateTime(timestamp);
	}
	
	public static void main(String[] args) {
		String fmt = "7/23/2017, 10:50:00 AM";
		System.out.println(new LocalDateTime(fmt));
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
