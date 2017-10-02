package com.dk.trender.core;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:42
 */
public class Post {
    private @NotEmpty String id;
	private @NotEmpty String type;
	private @NotEmpty String authorName;
	private @NotEmpty String source;

	private @NotEmpty String link;
	private @NotEmpty String description = "";
	private @NotEmpty String location = "worldwide";
	private @NotEmpty String lang = "en-us";
	private @NotNull DateTime timestamp;
	private DateTime indexedAt;

	private @NotNull String authorPicture = "";
	private String authorId = "";
	private @NotNull String picture = "";
	private @NotNull String data = "{}";
	private String cached = "";
	private @NotNull List<String> category = Collections.emptyList();	
	
	@JsonProperty
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	
	@JsonProperty
	public String getAuthorId() {
		return authorId;
	}

	@JsonIgnore
	public Post indexedAt(DateTime date) {
		this.indexedAt = date;
		return this;
	}

	@JsonProperty
	public DateTime indexedAt() {
		return indexedAt;
	}

	@JsonProperty
	public Post setLang(String lang) {
		this.lang = lang;
		return this;
	}

	@JsonProperty
	public String getLang() {
		return lang;
	}

	@JsonProperty
	public String getId() {
		return id;
	}

	@JsonProperty
	public Post setId(String id) {
		this.id = id;
		return this;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}

	@JsonProperty
	public Post setDescription(String description) {
		this.description = description;
		return this;
	}

	@JsonProperty
	public void setLocation(String location) {
		this.location = location;
	}

	@JsonProperty
	public String getLocation() {
		return location;
	}

	@JsonProperty
	public Post setPicture(String picture) {
		this.picture = picture;
		return this;
	}

	@JsonProperty
	public String getPicture() {
		return picture;
	}

	@JsonProperty
	public Post setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	@JsonProperty
	public Post setTimestamp(String ts) {
		this.timestamp = new DateTime(ts.replace(" ", "T"));
		return this;
	}

	@JsonProperty
	public DateTime getTimestamp() {
		return timestamp;
	}

 	@JsonProperty
 	public String timestampFmt() {
		return DateTimeFormat.forPattern("YYY-MM-dd HH:mm:ss")
				.print(timestamp)
				.replace(' ', 'T');
 	}

 	@JsonProperty
 	public String indexedAtFmt() {
		return DateTimeFormat.forPattern("YYY-MM-dd HH:mm:ss")
				.print(indexedAt)
				.replace(' ', 'T');
 	}

	@JsonProperty
	public String getType() {
		return type;
	}

	@JsonProperty
	public Post setType(String type) {
		this.type = type;
		return this;
	}

	@JsonProperty
	public Post setAuthorName(String name) {
		this.authorName = name;
		return this;
	}

	@JsonProperty
	public String getAuthorName() {
		return authorName;
	}

	@JsonProperty
	public Post setAuthorPicture(String pic) {
		this.authorPicture = pic;
		return this;
	}

	@JsonProperty
	public String getAuthorPicture() {
		return authorPicture;
	}

	@JsonProperty
	public Post setSource(String source) {
		this.source = source;
		return this;
	}

	@JsonProperty
	public String getSource() {
		return source;
	}

	@JsonProperty
	public Post setLink(String link) {
		this.link = link;
		return this;
	}

	@JsonProperty
	public String getLink() {
		return link;
	}

	@JsonProperty
	public Post setData(String blob) {
		this.data = blob;
		return this;
	}

	@JsonProperty
	public String getData() {
		return data;
	}

	@JsonProperty
	public void setCategory(List<String> category) {
		this.category = category.stream()
				.map(t -> t.toLowerCase().trim())
				.collect(Collectors.toList());
	}

	@JsonProperty
	public List<String> getCategory() {
		return category;
	}

	@JsonProperty
	public void setCached(String cached) {
		this.cached = cached;
	}

	@JsonProperty
	public String getCached() {
		return cached;
	}

	@JsonIgnore
	public SolrInputDocument toDoc() {
		final Post post = this;
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", post.getId());
		doc.addField("type", post.getType());
		doc.addField("authorName", post.getAuthorName());
		doc.addField("source", post.getSource());
		doc.addField("link", post.getLink());
		doc.addField("description", post.getDescription());
		doc.addField("timestamp", post.timestampFmt() );
		doc.addField("location", post.getLocation());
		doc.addField("indexedAt", post.indexedAtFmt());
		doc.addField("lang", post.getLang());
		doc.addField("cached", post.getCached());
		doc.addField("authorId", post.getAuthorId());

		// the optional fellas
		doc.addField("authorPicture", post.getAuthorPicture());
		doc.addField("picture", post.getPicture());
		doc.addField("data", post.getData());
		doc.addField("category", post.getCategory());	
		return doc;
	}

	public static Post fromDoc(SolrDocument doc) {		
		Post p = new Post();
		p.setId(doc.get("id").toString());

		p.setType(doc.get("type").toString());
		p.setAuthorName(doc.get("authorName").toString());
		p.setSource(doc.get("source").toString());
		p.setLink(doc.get("link").toString());
		p.setDescription(doc.get("description").toString());
		p.setTimestamp(new DateTime(doc.get("timestamp")));
		p.setLocation(doc.get("location").toString());
		p.indexedAt(new DateTime(doc.get("indexedAt")));
		p.setLang(Optional.ofNullable(doc.get("lang")).orElse("en-us").toString());
		p.setAuthorId(Optional.ofNullable(doc.get("authorId")).orElse("i/unknown").toString());

		p.setAuthorPicture(Optional.ofNullable(doc.get("authorPicture")).orElse("").toString());
		p.setPicture(Optional.ofNullable(doc.get("picture")).orElse("").toString());
		p.setCached(Optional.ofNullable(doc.get("cached")).orElse("").toString());
		p.setData(doc.get("data").toString());
		p.setCategory((List<String>)doc.get("category"));

		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * XXX: since comparison is done in terms of id
	 * 		what about an update?
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;			
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		final Post that = (Post) obj;
		return Objects.equal(this.id, that.id);
	}
}