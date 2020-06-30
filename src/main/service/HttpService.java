package main.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Class to make httpCalls
 * 
 * @author nicol
 *
 */
public class HttpService {

	/**
	 * Get Html content from http or https adresses
	 * 
	 * @param urlStr
	 * @param isHttps
	 * @return
	 * @throws IOException
	 */
	public static String getContent(String urlStr, boolean isHttps)
			throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection con;
		if (isHttps) {
			con = (HttpsURLConnection) url.openConnection();
		} else {
			con = (HttpURLConnection) url.openConnection();
		}
		// call
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");

		// response
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();

		con.disconnect();
		return content.toString();

	}
}
