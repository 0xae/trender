package com.dk.trender.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ZMediaService {
	private static final Logger log = LoggerFactory.getLogger(ZMediaService.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	private final String mediaHost;	

	public ZMediaService(String mediaHost) {
		this.mediaHost = mediaHost;
	}
	
	public String store(String link, String container, String name) throws JsonProcessingException {
		int index = link.lastIndexOf('.');
		String ext = (index == -1) ? ".jpg" : link.substring(index);
		if (ext.indexOf('?') != -1) {
			ext = ext.substring(0, ext.indexOf('?'));
		}

		String path = mediaHost + container + "/" + name + ext;
		InputStream input = null;
		OutputStream output = null;

		try {
			new File(mediaHost + container).mkdirs();
			URL u = new URL(link);
			File out = new File(path);
			if (out.exists()) {
				out.delete();				
			}
			out.createNewFile();

			input = new BufferedInputStream(u.openStream());
			output = new FileOutputStream(out);
			IOUtils.copy(input, output);
			return container + "/" + name + ext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (input != null)
					input.close();
				if (output != null)
					output.close();				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
