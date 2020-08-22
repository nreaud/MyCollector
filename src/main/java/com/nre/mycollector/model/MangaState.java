package com.nre.mycollector.model;

/**
 * This class represent the current state for each manga with the last chapter I
 * read and the last chapter available (cross sites) and its language
 * 
 * @author nicol
 *
 */
public class MangaState implements Comparable<MangaState> {
	private Manga manga;
	private float lastRead;
	private float lastAvailable;
	private Language lastAvailableLanguage;
	private boolean toRead;

	public MangaState() {
		// NullPointer proof
		this.manga = Manga.UNKNOWN;
		this.lastRead = -1;
		this.lastAvailable = -1;
		this.lastAvailableLanguage = Language.SPOIL;
	}

	public MangaState(Manga manga, float lastAvailable, Language lastAvailableLanguage) {
		this.manga = manga;
		this.lastAvailable = lastAvailable;
		this.lastAvailableLanguage = lastAvailableLanguage;
		updateToRead();
	}

	public MangaState(Manga manga, float lastRead, float lastAvailable, Language lastAvailableLanguage) {
		this.manga = manga;
		this.lastRead = lastRead;
		this.lastAvailable = lastAvailable;
		this.lastAvailableLanguage = lastAvailableLanguage;
		updateToRead();
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	public Language getLastAvailableLanguage() {
		return lastAvailableLanguage;
	}

	public void setLastAvailableLanguage(Language lastAvailableLanguage) {
		this.lastAvailableLanguage = lastAvailableLanguage;
	}

	public float getLastRead() {
		return lastRead;
	}

	public void setLastRead(float lastRead) {
		this.lastRead = lastRead;
	}

	public float getLastAvailable() {
		return lastAvailable;
	}

	public void setLastAvailable(float lastAvailable) {
		this.lastAvailable = lastAvailable;
	}

	public boolean isToRead() {
		return toRead;
	}

	public void setToRead(boolean toRead) {
		this.toRead = toRead;
	}

	public MangaState updateToRead() {
		int nbChapterstoRead = Math.round(lastAvailable - lastRead); //arrondi au dessus si chap 0.5
		toRead = (nbChapterstoRead == 1 && Language.atLeast(lastAvailableLanguage, Language.ENGLISH))
		    || (nbChapterstoRead > 1);  //si 1 chapitre à lire, il faut au moins anglais
		return this;
	}

	@Override
	public int compareTo(MangaState other) {
		int res;
		if (this == other) {
			res = 0;
		} else if (other == null) {
			res = 1;
		} else {
			if (this.manga != other.manga) {
				res = this.manga.compareTo(other.manga); // ordered by name
			} else { // same manga
				if (this.lastAvailable != other.lastAvailable) {
					res = this.lastAvailable > other.lastAvailable ? 1 : -1;
				} else { // same chapter number
					res = this.lastAvailableLanguage.compareTo(other.lastAvailableLanguage);
				}
			}
		}
		return res;
	}

}
