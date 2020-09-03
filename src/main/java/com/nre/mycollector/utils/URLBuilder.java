package com.nre.mycollector.utils;

public class URLBuilder {

	private static final char SLASH = '/';

	private URLBuilder() {
		//hidden
	}

	public static String addSlashAtEndIfNeeded(final String url) {
		String res = url;
		char lastChar = url.charAt(url.length() - 1);
		if (lastChar != SLASH) {
			res += SLASH;
		}
		return res;
	}

}
