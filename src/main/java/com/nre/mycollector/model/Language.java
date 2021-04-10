package com.nre.mycollector.model;

/**
 * The language available for manga
 * 
 * @author nicol
 *
 */
public enum Language {
	UNKNOWN, SPOIL, RAW, ENGLISH, FRENCH; // ordered from "less ready" to "most ready"

	public static boolean atLeast(Language language, Language minimumLanguage) {
		return language.ordinal() >= minimumLanguage.ordinal();
	}

	public static boolean moreRecent(Language newLanguage, Language currentLanguage) {
		return newLanguage.ordinal() > currentLanguage.ordinal();
	}

	public static Language convertFromString(String uppercaseName) {
		Language res = Language.UNKNOWN;
		if (uppercaseName.equals("VF") | uppercaseName.equals("FRENCH")) {
			res = Language.FRENCH;
		} else if (uppercaseName.equals("VA") || uppercaseName.equals("VUS") || uppercaseName.equals("ENGLISH")) {
			res = Language.ENGLISH;
		} else if (uppercaseName.equals("RAW")) {
			res = Language.RAW;
		} else if (uppercaseName.equals("SPOIL")) {
			res = Language.SPOIL;
		}

		return res;
	}

}