package com.nre.mycollector.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

/**
 * Class to make httpCalls
 * 
 * @author nicol
 *
 */
@Service
public class HttpService {

	private static final Pattern isHttpsPattern = Pattern.compile("https.*");

	/**
	 * Get Html content from http or https adresses
	 * 
	 * @param urlStr
	 * @param isHttps
	 * @return
	 * @throws IOException
	 */
	public String getContent(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection con;
		if (isHttps(urlStr)) {
			con = (HttpsURLConnection) url.openConnection();
		} else {
			con = (HttpURLConnection) url.openConnection();
		}
		// call
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");

		// response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();

		con.disconnect();
		return content.toString();

	}

	private boolean isHttps(String urlStr) {
		return isHttpsPattern.matcher(urlStr).matches();
	}
}
