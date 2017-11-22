package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	@Column(name="name")
	private String name;

	@Column(name="picture")
	private String picture;

	@Column(name="query_conf")
	@ColumnTransformer(write="?::jsonb")
	private String queryConf="{}";

	@Column(name="curation")
	private int curation=0;

	@Column(name="rank", nullable=false)
	private int rank=-1;

	@Column(name="inteligence", nullable=false)
	@ColumnTransformer(write="?::jsonb")
	private String intel="{}";

	@NotEmpty
	@Column(name="audience", nullable=false)
	@OneOf({PUBLIC, PRIVATE})
	private String audience = PRIVATE;

	@Column(name="created_at", updatable=false)
	private DateTime createdAt=new DateTime();

	@Column(name="last_update")
	private DateTime lastUpdate=new DateTime();

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
}
