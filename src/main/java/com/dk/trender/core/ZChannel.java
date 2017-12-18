package com.dk.trender.core;

import java.util.ArrayList;
import java.util.Collections;
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
	private String queryConf="{}";

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
	public void totalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	@JsonIgnore
	public int totalCount() {
		return totalCount;
	}

	@JsonProperty
	public void setCollections(List<ZCollection> collections) {
		this.collections = collections;
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
	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
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
	public void setId(long id) {
		this.id = id;
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
	public String getPicture() {
		return picture;
	}

	@JsonProperty
	public void setPicture(String picture) {
		this.picture = picture;
	}

	@JsonProperty
	public String getQueryConf() {
		return queryConf;
	}

	@JsonIgnore
	public QueryConf queryConf() {
		return QueryConf.parse(this.queryConf);
	}

	@JsonIgnore
	public QueryConf queryConf(QueryConf c) {
		this.queryConf = c.toString();
		return c;
	}

	@JsonProperty
	public void setQueryConf(String queryConf) {
		this.queryConf = queryConf;
	}

	@JsonProperty
	public int getCuration() {
		return curation;
	}

	@JsonProperty
	public void setCuration(int curation) {
		this.curation = curation;
	}

	@JsonProperty
	public int getRank() {
		return rank;
	}

	@JsonProperty
	public void setRank(int rank) {
		this.rank = rank;
	}

	@JsonProperty
	public String getIntel() {
		return intel;
	}

	@JsonProperty
	public void setIntel(String inteligence) {
		this.intel = inteligence;
	}

	@JsonProperty
	public String getAudience() {
		return audience;
	}

	@JsonProperty
	public void setAudience(String audience) {
		this.audience = audience;
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
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}
}
