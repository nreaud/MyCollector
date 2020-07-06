package model;

/**
 * Describe a new state for a manga (mangaName + chapterNumber + language)
 * 
 * @author nicol
 *
 */
public class Release {
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
		return "Release [manga=" + manga + ", number=" + number + ", language="
				+ language + "]";
	}

}
