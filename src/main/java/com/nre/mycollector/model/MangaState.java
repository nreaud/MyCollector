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
	private Short lastRead;
	private Short lastAvailable;
	private Language lastAvailableLanguage;

	public MangaState() {
		// NullPointer proof
		this.manga = Manga.UNKNOWN;
		this.lastRead = -1;
		this.lastAvailable = -1;
		this.lastAvailableLanguage = Language.SPOIL;
	}

	public MangaState(Manga manga, Short lastAvailable, Language lastAvailableLanguage) {
		this.manga = manga;
		this.lastAvailable = lastAvailable;
		this.lastAvailableLanguage = lastAvailableLanguage;
	}

	public MangaState(Manga manga, Short lastRead, Short lastAvailable, Language lastAvailableLanguage) {
		this.manga = manga;
		this.lastRead = lastRead;
		this.lastAvailable = lastAvailable;
		this.lastAvailableLanguage = lastAvailableLanguage;
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

	public Short getLastRead() {
		return lastRead;
	}

	public void setLastRead(Short lastRead) {
		this.lastRead = lastRead;
	}

	public Short getLastAvailable() {
		return lastAvailable;
	}

	public void setLastAvailable(Short lastAvailable) {
		this.lastAvailable = lastAvailable;
	}

	public static boolean moreRecent(MangaState currentState, MangaState newState) {
		return (newState.getLastAvailable() > currentState.getLastAvailable())
		    || ((newState.getLastAvailable().equals(currentState.getLastAvailable()))
		        && (Language.moreRecent(newState.getLastAvailableLanguage(), currentState.getLastAvailableLanguage())));
	}

	@Override
	public String toString() {
		return "State [manga=" + manga + ", lastRead=" + lastRead + ", lastAvailable=" + lastAvailable
		    + ", lastAvailableLanguage=" + lastAvailableLanguage + "]";
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
				if (!this.lastAvailable.equals(other.lastAvailable)) {
					res = this.lastAvailable - other.lastAvailable;
				} else { // same chapter number
					res = this.lastAvailableLanguage.compareTo(other.lastAvailableLanguage);
				}
			}
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastAvailable == null) ? 0 : lastAvailable.hashCode());
		result = prime * result + ((lastAvailableLanguage == null) ? 0 : lastAvailableLanguage.hashCode());
		result = prime * result + ((lastRead == null) ? 0 : lastRead.hashCode());
		result = prime * result + ((manga == null) ? 0 : manga.hashCode());
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
		MangaState other = (MangaState) obj;
		if (lastAvailable == null) {
			if (other.lastAvailable != null)
				return false;
		} else if (!lastAvailable.equals(other.lastAvailable))
			return false;
		if (lastAvailableLanguage != other.lastAvailableLanguage)
			return false;
		if (lastRead == null) {
			if (other.lastRead != null)
				return false;
		} else if (!lastRead.equals(other.lastRead))
			return false;
		if (manga != other.manga)
			return false;
		return true;
	}

}
