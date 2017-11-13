package com.dk.trender.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZPost;
import com.dk.trender.exceptions.BadRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ZMediaService {
	private static final Logger log = LoggerFactory.getLogger(ZMediaService.class);
	private final String mediaHost;	

	public ZMediaService(String mediaHost) {
		this.mediaHost = mediaHost;
	}

	public String store(String link, String container, String name) {
		int index = link.lastIndexOf('.');
		String ext = (index == -1) ? ".jpg" : link.substring(index);
		if (ext.indexOf('?') != -1) {
			ext = ext.substring(0, ext.indexOf('?'));
		}

		String path = mediaHost + container + "/" + name + ext;
		InputStream input = null;
		OutputStream output = null;
		URL outUrl = null;

		try {
			outUrl = new URL(link);
			new File(mediaHost + container).mkdirs();
			File out = new File(path);
			if (out.exists()) {
				out.delete();				
			}
			out.createNewFile();

			input = new BufferedInputStream(outUrl.openStream());
			output = new FileOutputStream(out);
			IOUtils.copy(input, output);
			return container + "/" + name + ext;
		} catch(java.net.UnknownHostException eu){
			String msg = outUrl.getHost() + " is not available";
			throw new BadRequest(503, Arrays.asList(msg));
		} catch (MalformedURLException ek) {
			String msg = "Malformed url: '" + link + "'.";
			throw new BadRequest(400, Arrays.asList(msg));			
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
