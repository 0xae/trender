package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="z_post_media")
public class PostMedia {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	@Column(name="title", nullable=false)
	private String title;

	@NotNull
	@Column(name="description", nullable=false)
	private String description;

	@Column(name="timestamp", nullable=false)
	private LocalDateTime time;

	@Column(name="indexed_at", nullable=false)
	private LocalDateTime indexedAt;

	@NotEmpty
	@Column(name="type", nullable=false)
	private String type;

	@NotEmpty
	@Column(name="source", nullable=false)
	private String source;	

	@NotEmpty
	@Column(name="data", nullable=false)
    @ColumnTransformer(write="?::jsonb")
	private String data;

	@Column(name="post_id", nullable=false, updatable=false)
	private long postId;

	@Column(name="listing_id", nullable=false, updatable=false)
	private long listingId;	

	@NotEmpty
	@Column(name="ref", nullable=false, updatable=false)
	private String ref;

	@ManyToOne
	@JoinColumn(name="post_id", insertable=false, 
				updatable=false, 
				foreignKey=@ForeignKey(name="post_id_fkey"))
	private Post post;

	@ManyToOne
	@JoinColumn(name="listing_id", insertable=false, 
				updatable=false, 
				foreignKey=@ForeignKey(name="listing_id_fkey"))
	private Listing listing;

	@JsonProperty
	public void setListing(Listing listing) {
		this.listing = listing;
	}
	
	@JsonProperty
	public Listing getListing() {
		return listing;
	}
	
	@JsonProperty
	public void setPost(Post post) {
		this.post = post;
	}
	
	@JsonProperty
	public Post getPost() {
		return post;
	}

	@JsonProperty
	public void setListingId(long listingId) {
		this.listingId = listingId;
	}

	@JsonProperty
	public long getListingId() {
		return listingId;
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
	public void setRef(String ref) {
		this.ref = ref;
	}

	@JsonProperty
	public String getRef() {
		return ref;
	}

	@JsonProperty
	public long getPostId() {
		return postId;
	}

	@JsonProperty
	public void setPostId(long postId) {
		this.postId = postId;
	}

	@JsonProperty
	public String getData() {
		return data;
	}

	@JsonProperty
	public void setData(String data) {
		this.data = data;
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
	public String getTitle() {
		return title;
	}

	@JsonProperty
	public void setTitle(String title) {
		this.title = title;
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
	public LocalDateTime getIndexedAt() {
		return indexedAt;
	}

	@JsonProperty
	public void setIndexedAt(LocalDateTime indexedAt) {
		this.indexedAt = indexedAt;
	}

	@JsonProperty
	public LocalDateTime getTime() {
		return time;
	}

	@JsonProperty
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	@JsonProperty
	public String getTimeFmt() {
		return time.toString();
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
