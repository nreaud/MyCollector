package com.nre.mycollector.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringUtils {

	private StringUtils() {
		//hidden
	}

	/**
	 * Call textContainedInAtLeastOne(String text, boolean caseSensitive,
	 * String... potentialContainers) with caseSensitive false
	 * 
	 * @param text
	 * @param potentialContainers
	 * @return
	 */
	public static boolean textContainedInAtLeastOne(String text, String... potentialContainers) {
		return textContainedInAtLeastOne(text, false, potentialContainers);
	}

	//TODO test
	public static boolean textContainedInAtLeastOne(String[] texts, String... potentialContainers) {
		boolean found = false;
		int i = 0;
		while (!found && i < texts.length) {
			found = textContainedInAtLeastOne(texts[i], false, potentialContainers);
			i++;
		}
		return found;
	}

	public static boolean textContainedInAtLeastOne(String text, boolean caseSensitive, String... potentialContainers) {
		boolean res;
		if (!caseSensitive) {
			res = Arrays.stream(potentialContainers).anyMatch(potentialContainer -> Pattern
			    .compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(potentialContainer).find());
		} else {
			res = Arrays.stream(potentialContainers).anyMatch(potentialContainer -> potentialContainer.contains(text));
		}
		return res;
	}

}
