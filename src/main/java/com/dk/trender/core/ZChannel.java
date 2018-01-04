package com.dk.trender.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.dk.trender.service.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.validation.OneOf;

@Entity
@Table(name="z_channel")
public class ZChannel {
	public static final String INTERNAL_PREFIX = "t-";
	public static final String PUBLIC="public";
	public static final String PRIVATE="private";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	@Column
	private String name;

	@Column
	private String description="";

	@Column
	private String picture = "";

	@Column(name="query_conf")
	@ColumnTransformer(write="?::jsonb")
	private String queryConf;

	@Column
	private int curation=0;

	@Column(nullable=false)
	private int rank=-1;

	@Column(name="inteligence", nullable=false)
	@ColumnTransformer(write="?::jsonb")
	private String intel="{}";

	@NotEmpty
	@Column(nullable=false)
	@OneOf({PUBLIC, PRIVATE})
	private String audience = PUBLIC;

	@Column(name="created_at", updatable=false)
	private DateTime createdAt=new DateTime();

	@Column(name="last_update")
	private DateTime lastUpdate=new DateTime();

	@Column(name="last_access")
	private DateTime lastAccess=new DateTime();
	
	@Transient
	private List<ZCollection> collections = new ArrayList<>();

	@Transient
	private int totalCount;

	@JsonIgnore
	public ZChannel totalCount(int totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	@JsonIgnore
	public int totalCount() {
		return totalCount;
	}

	@JsonProperty
	public ZChannel setCollections(List<ZCollection> collections) {
		this.collections = collections;
		return this;
	}

	@JsonProperty
	public List<ZCollection> getCollections() {
		return collections;
	}

	@JsonProperty
	public ZChannel setLastAccess(DateTime lastAccess) {
		this.lastAccess = lastAccess;
		return this;
	}

	@JsonProperty
	public DateTime getLastAccess() {
		return lastAccess;
	}

	public String getLastAccessFmt() {
		return Utils.format(lastAccess);
	}

	@JsonProperty
	public ZChannel setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
		return this;
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
	public long getId() {
		return id;
	}

	@JsonProperty
	public ZChannel setId(long id) {
		this.id = id;
		return this;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public ZChannel setName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty
	public String getPicture() {
		return picture;
	}

	@JsonProperty
	public ZChannel setPicture(String picture) {
		this.picture = picture;
		return this;
	}

	@JsonProperty
	public String getQueryConf() {
		return queryConf;
	}

	@JsonIgnore
	public ZSearch queryConf() {
		return ZSearch.parse(this.queryConf);
	}

	@JsonIgnore
	public ZSearch queryConf(ZSearch c) {
		this.queryConf = c.toString();
		return c;
	}

	@JsonProperty
	public ZChannel setQueryConf(String queryConf) {
		this.queryConf = queryConf;
		return this;
	}

	@JsonProperty
	public int getCuration() {
		return curation;
	}

	@JsonProperty
	public ZChannel setCuration(int curation) {
		this.curation = curation;
		return this;
	}

	@JsonProperty
	public int getRank() {
		return rank;
	}

	@JsonProperty
	public ZChannel setRank(int rank) {
		this.rank = rank;
		return this;
	}

	@JsonProperty
	public String getIntel() {
		return intel;
	}

	@JsonProperty
	public ZChannel setIntel(String inteligence) {
		this.intel = inteligence;
		return this;
	}

	@JsonProperty
	public String getAudience() {
		return audience;
	}

	@JsonProperty
	public ZChannel setAudience(String audience) {
		this.audience = audience;
		return this;
	}

	@JsonProperty
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@JsonProperty
	public ZChannel setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	@JsonProperty
	public ZChannel setDescription(String description) {
		this.description = description;
		return this;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}
}
