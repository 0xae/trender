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

import com.dk.trender.core.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MediaService {
	public static final String STORE = "/opt/lampp/htdocs/trender/downloads/media/";	
	private static final Logger log = LoggerFactory.getLogger(MediaService.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public String store(Post p, String name) throws JsonProcessingException {
		String link = p.getPicture();
		int index = link.lastIndexOf('.');
		String ext = (index == -1) ? ".jpg" : link.substring(index);
		if (ext.indexOf('?') != -1) {
			ext = ext.substring(0, ext.indexOf('?'));
		}

		String path = STORE + name + ext;
		InputStream input = null;
		OutputStream output = null;

		try {
			URL u = new URL(link);
			File out = new File(path);
			if (out.exists()) {
				out.delete();				
			}
			out.createNewFile();

			input = new BufferedInputStream(u.openStream());
			output = new FileOutputStream(out);
			IOUtils.copy(input, output);
			return "downloads/media/" + name + ext;
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
