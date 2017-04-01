package com.dk.trender.core;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
@Access(AccessType.FIELD)
public class PostReaction {
	@Column(name="count_likes")
	private long countLikes;

	@Column(name="count_love")
	private long countLove;

	@Column(name="count_haha")
	private long countHaha;

	@Column(name="count_wow")
	private long countWow;

	@Column(name="count_sad")
	private long coundSad;

	@Column(name="count_angry")
	private long countAngry;

	@JsonProperty
	public long getCountLikes() {
		return countLikes;
	}

	@JsonProperty
	public void setCountLikes(long countLikes) {
		this.countLikes = countLikes;
	}

	@JsonProperty
	public long getCountLove() {
		return countLove;
	}

	@JsonProperty
	public void setCountLove(long countLove) {
		this.countLove = countLove;
	}

	@JsonProperty
	public long getCountHaha() {
		return countHaha;
	}

	@JsonProperty
	public void setCountHaha(long countHaha) {
		this.countHaha = countHaha;
	}

	@JsonProperty
	public long getCountWow() {
		return countWow;
	}

	@JsonProperty
	public void setCountWow(long countWow) {
		this.countWow = countWow;
	}

	@JsonProperty
	public long getCoundSad() {
		return coundSad;
	}

	@JsonProperty
	public void setCoundSad(long coundSad) {
		this.coundSad = coundSad;
	}

	@JsonProperty
	public long getCountAngry() {
		return countAngry;
	}

	@JsonProperty
	public void setCountAngry(long countAngry) {
		this.countAngry = countAngry;
	}	
}
