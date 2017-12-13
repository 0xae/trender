package com.dk.trender.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.dk.trender.core.feed.ZGroup;
import com.dk.trender.exceptions.BadRequest;
import com.dk.trender.service.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.validation.OneOf;

@Entity
@Table(name="z_collection")
public class ZCollection {
	public static final String NAME = "[a-zA-Z0-9_@.-]+";
	public static final String PUBLIC = "public";
	public static final String PRIVATE = "private";
	private static final int MAX_TYPES = 5;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	@Column(updatable=false)
	@Pattern(regexp=NAME)
	private String name;

	@NotEmpty
	@Column
	@Length(min=3, max=50)
	private String label;

	@NotEmpty
	@OneOf({PUBLIC, PRIVATE})
	@Column(nullable=false)
	private String audience=PUBLIC;

	@Column(name="created_at", updatable=false)
	private DateTime createdAt=new DateTime();

	@Column(name="last_update")
	private DateTime lastUpdate=new DateTime();

	@Column(name="channel_id", nullable=true)
	private Long channelId;

	@Column
	private boolean display=false;

	@Column(nullable=false)
	private BigDecimal curation = BigDecimal.valueOf(0.1);

	@Column(nullable=false)
	private boolean update=true;

	@Transient
	private List<ZPost> posts = new ArrayList<>();

	@Transient
	private List<ZGroup> groups = new ArrayList<>();
	
	@Column
	@ColumnTransformer(write="?::jsonb")
	private String conf = "{}";

	@Column
	@NotEmpty
	private String feed;

	@Column
	@ColumnTransformer(write="regexp_split_to_array(?, ',')",
			   read="array_to_string(types, ',')")
	private String types;

	@JsonProperty
	public void setTypes(List<String> types) {
		if (types.size() > MAX_TYPES) {
			String msg = "Only " + MAX_TYPES + " allowed!";
			throw new BadRequest(Arrays.asList(msg));
		}

		this.types = types.stream()
						.map(t -> t.trim().toLowerCase())
						.distinct()
						.collect(Collectors.joining(","));
	}

	@JsonProperty
	public List<String> getTypes() {
		return Arrays.asList(types.split(","));
	}

	@JsonProperty
	public void setFeed(String feed) {
		this.feed = feed;
	}

	@JsonProperty
	public String getFeed() {
		return feed;
	}

	@JsonProperty
	public String getConf() {
		return conf;
	}

	@JsonProperty
	public void setConf(String conf) {
		this.conf = conf;
	}

	@JsonIgnore
	public QueryConf queryConf() {
		return QueryConf.parse(conf);
	}

	@JsonIgnore
	public QueryConf queryConf(QueryConf value) {
		this.conf = value.toString();
		return value;
	}

	@JsonProperty
	public void setGroups(List<ZGroup> groups) {
		this.groups = groups;
	}

	@JsonProperty
	public List<ZGroup> getGroups() {
		return groups;
	}

	@JsonProperty
	public void setPosts(List<ZPost> posts) {
		this.posts = posts;
	}

	@JsonProperty
	public List<ZPost> getPosts() {
		return posts;
	}

	@JsonProperty
	public BigDecimal getCuration() {
		return curation;
	}

	@JsonProperty
	public void setCuration(BigDecimal curation) {
		this.curation = curation;
	}

	@JsonProperty
	public boolean isUpdate() {
		return update;
	}

	@JsonProperty
	public void setUpdate(boolean update) {
		this.update = update;
	}

	@JsonProperty
	public void setDisplay(boolean display) {
		this.display = display;
	}

	@JsonProperty
	public boolean isDisplay() {
		return display;
	}

	@JsonProperty
	public void setLabel(String label) {
		this.label = label;
	}

	@JsonProperty
	public String getLabel() {
		return label;
	}

	@JsonProperty
	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public void setAudience(String audience) {
		this.audience = audience;
	}

	@JsonProperty
	public String getAudience() {
		return audience;
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
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@JsonProperty
	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty
	public DateTime getLastUpdate() {
		return lastUpdate;
	}

	@JsonProperty
	public String getLastUpdateFmt() {
		return Utils.format(lastUpdate);
	}	

	@JsonProperty
	public String getCreatedAtFmt() {
		return Utils.format(createdAt);
	}		

	@JsonProperty
	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@JsonProperty
	public Long getChannelId() {
		return channelId;
	}

	@JsonProperty
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}
