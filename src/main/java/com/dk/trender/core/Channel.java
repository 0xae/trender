package com.dk.trender.core;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="z_channel")
public class Channel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	@Column(name="name", nullable=false)
	private String name;

	@Column(name="description")
	private String description;

	@NotEmpty
	@Column(name="topic", nullable=false)
	private String topic;

	@NotEmpty
	@Column(name="post_types", nullable=false)
	@ColumnTransformer(write="regexp_split_to_array(?, ',')",
					   read="array_to_string(post_types, ',')")
	private String postTypes;

	@NotNull
	@Column(name="creation_date", nullable=false, updatable=false)
	private LocalDateTime creationDate = new LocalDateTime();

	@JsonProperty
	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty
	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
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
	public String getTopic() {
		return topic;
	}

	@JsonProperty
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@JsonProperty
	public List<String> getSpiders() {
		return Arrays.asList(postTypes.split(","));
	}

	@JsonProperty
	public void setSpiders(@NotEmpty List<String> s) {
		this.postTypes = String.join(",", s);
	}
}