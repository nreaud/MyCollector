package model;
/**
 * The language available for manga
 * 
 * @author nicol
 *
 */
public enum Language {
	SPOIL, RAW, ENGLISH, FRENCH; // ordered from "less ready" to "most ready"

	public static boolean moreRecent(Language newLanguage,
			Language currentLanguage) {
		return newLanguage.ordinal() > currentLanguage.ordinal();
	}
}