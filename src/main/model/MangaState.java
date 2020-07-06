package model;

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

}
