package com.dk.trender.core.groups;

import java.util.List;
import java.util.Map;

import com.dk.trender.core.ZGroup;
import com.dk.trender.core.ZPost;

public interface PostGroup {
	public List<ZGroup> process(Map<String, List<ZPost>> posts);
}
