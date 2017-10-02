package com.dk.trender.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.validation.OneOf;

@Entity
@Table(name="z_timeline")
public class Timeline {
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
	@Column(name="created_at", nullable=false, updatable=false)
	private DateTime creationDate = new DateTime();

	@Column(name="updated_at", nullable=false, updatable=false)
	private DateTime updateDate;

	@Column(name="index", nullable=false)
	private int index = 0;

	@Column(name="state", nullable=false)
	@OneOf({"temp", "created"})
	private String state = "temp";

	@NotNull
	@Column(name="is_active", nullable=false)
	private boolean isActive = true;	

	@JsonProperty
	public int getIndex() {
		return index;
	}

	@JsonIgnore
	public void index(int i) {
		this.index = i;
	}

	@JsonProperty
	public String getState() {
		return state;
	}

	@JsonProperty
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty
	public void setPostTypes(String postTypes) {
		this.postTypes = postTypes;
	}

	@JsonProperty
	public String getPostTypes() {
		return postTypes;
	}

	@JsonProperty
	public DateTime getUpdateDate() {
		return updateDate;
	}

	@JsonProperty
	public void setUpdateDate(DateTime updateDate) {
		this.updateDate = updateDate;
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
	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty
	public DateTime getCreationDate() {
		return creationDate;
	}

 	@JsonProperty
 	public String creationDateFmt() {
		return DateTimeFormat.forPattern("YYY-MM-d HH:mm:ss")
				.print(creationDate)
				.replace(' ', 'T');
 	}	

 	@JsonProperty
 	public String updateDateFmt() {
		return DateTimeFormat.forPattern("YYY-MM-d HH:mm:ss")
				.print(updateDate)
				.replace(' ', 'T');
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

	@JsonProperty
	public boolean isActive() {
		return isActive;		
	}

	@JsonProperty
	public void setIsActive(boolean flag) {
		this.isActive = flag;		
	}

	public static class Topic {
		private String name;
		private int score;

		public Topic() {
			// TODO Auto-generated constructor stub
		}

		public Topic(String name, int score) {
			this.name = name;
			this.score = score;
		}		

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public int getScore() {
			return score;
		}
	}

	public static class Stream {
		private Timeline timeline;
		private List<Post> posts;		
		private Map<String, List<Topic>> topics = new HashMap<>();
		private int count;

		public Stream() {
			// TODO Auto-generated constructor stub
		}

		public Stream (Timeline tl, List<Post> list) {
			this.setTimeline(tl);
			this.setPosts(list);
			this.count = list.size();
		}

		public Stream addTopic(String key, Topic topic) {
			List<Topic> ary = topics.getOrDefault(key, new ArrayList<>());
			ary.add(topic);
			topics.put(key, ary);
			return this;
		}

		@JsonProperty
		public Map<String, List<Topic>> getTopics() {
			return topics;
		}

		@JsonProperty
		public void setCount(int count) {
			this.count = count;
		}

		@JsonProperty
		public int getCount() {
			return count;
		}

		@JsonProperty
		public void setPosts(List<Post> posts) {
			this.posts = posts;
		}

		@JsonProperty
		public List<Post> getPosts() {
			return posts;
		}

		@JsonProperty
		public void setTimeline(Timeline timeline) {
			this.timeline = timeline;
		}

		@JsonProperty
		public Timeline getTimeline() {
			return timeline;
		}
	}
}