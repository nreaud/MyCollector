package com.nre.mycollector.model;

/**
 * Describe a new state for a manga (mangaName + chapterNumber + language)
 * 
 * @author nicol
 *
 */
public class Release implements Comparable<Release> {
	private Manga manga;
	private float number;
	private Language language;

	public Release() {
		super();
	}

	public Release(Manga name, float number, Language language) {
		super();
		this.manga = name;
		this.number = number;
		this.language = language;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	public float getNumber() {
		return number;
	}

	public void setNumber(float number) {
		this.number = number;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Release [manga=" + manga + ", number=" + number + ", language=" + language + "]";
	}

	@Override
	public int compareTo(Release other) {
		int res;
		if (this == other) {
			res = 0;
		} else if (other == null) {
			res = 1;
		} else {
			if (this.manga != other.manga) {
				res = this.manga.compareTo(other.manga); // ordered by name
			} else { // same manga
				if (this.number != other.number) {
					res = (int) (this.number - other.number);
				} else { // same chapter number
					res = this.language.compareTo(other.language);
				}
			}
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((manga == null) ? 0 : manga.hashCode());
		result = prime * result + Float.floatToIntBits(number);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Release other = (Release) obj;
		if (language != other.language)
			return false;
		if (manga != other.manga)
			return false;
		if (Float.floatToIntBits(number) != Float.floatToIntBits(other.number))
			return false;
		return true;
	}

}
