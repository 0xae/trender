package com.dk.trender.core;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.dk.trender.service.utils.Utils;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.validation.OneOf;

@Entity
@Table(name="z_collection")
public class ZCollection {
	public static final String NAMEP = "[a-zA-Z0-9_@.-]+";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	@Column(name="name", updatable=false)
	@Pattern(regexp=NAMEP)
	private String name;

	@NotEmpty
	@Column(name="label")
	@Length(min=3, max=50)
	private String label;

	@Column(name="description")
	private String description="";

	@NotEmpty
	@OneOf({"public", "private"})
	@Column(name="audience", nullable=false)
	private String audience="";	

	@Column(name="created_at", updatable=false)
	private DateTime createdAt=new DateTime();

	@Column(name="last_update")
	private DateTime lastUpdate=new DateTime();
	
	@Column(name="channel_id", nullable=true)
	private Long channelId;

	@Column(name="display")
	private boolean display=false;
	
	@Column(name="curation", nullable=false)
	private BigDecimal curation = BigDecimal.valueOf(0.5);

	@Column(name="update", nullable=false)
	private boolean update=true;
	

	public BigDecimal getCuration() {
		return curation;
	}

	public void setCuration(BigDecimal curation) {
		this.curation = curation;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	
	public boolean isDisplay() {
		return display;
	}

	public void setLabel(String label) {
		this.label = label;
	}

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
	public String getDescription() {
		return description;
	}

	@JsonProperty
	public void setDescription(String description) {
		this.description = description;
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
