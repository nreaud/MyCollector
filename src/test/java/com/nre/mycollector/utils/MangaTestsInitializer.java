package com.nre.mycollector.utils;

import java.util.HashMap;
import java.util.Map;

import com.nre.mycollector.model.Language;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;

public class MangaTestsInitializer {

	public static Map<Manga, MangaState> getInitCurrentState() {
		Map<Manga, MangaState> res = new HashMap<>();
		//AJIN - test update language
		res.put(Manga.AJIN, new MangaState(Manga.AJIN, 77, Language.ENGLISH));
		//BLACK CLOVER - test update number
		res.put(Manga.BLACK_CLOVER, new MangaState(Manga.BLACK_CLOVER, 251, 252, Language.FRENCH));
		return res;
	}

	public static Map<Manga, Release> getInitLireScanState() {
		Map<Manga, Release> res = new HashMap<>();
		//AJIN - test update language
		res.put(Manga.AJIN, new Release(Manga.AJIN, 77, Language.RAW));
		//BLACK CLOVER - test update number
		res.put(Manga.BLACK_CLOVER, new Release(Manga.BLACK_CLOVER, 251, Language.FRENCH));
		return res;
	}

	public static Map<Manga, MangaState> getInitCurrentStateToTestSortAlphabetic() {
		Map<Manga, MangaState> res = new HashMap<>();
		res.put(Manga.BLACK_CLOVER, new MangaState(Manga.BLACK_CLOVER, 251, 252, Language.FRENCH));
		res.put(Manga.AJIN, new MangaState(Manga.AJIN, 77, Language.ENGLISH));
		return res;
	}

	public static Map<Manga, MangaState> getInitCurrentStateToTestSortByToRead() {
		Map<Manga, MangaState> res = new HashMap<>();
		//AJIN - nothing new to read
		res.put(Manga.AJIN, new MangaState(Manga.AJIN, 77, 77, Language.ENGLISH));
		//BLACK CLOVER - to read
		res.put(Manga.BLACK_CLOVER, new MangaState(Manga.BLACK_CLOVER, 251, 252, Language.FRENCH));
		//BORUTO - to read + test 0.5
		res.put(Manga.BORUTO, new MangaState(Manga.BORUTO, 48, 48.5f, Language.ENGLISH));
		//MHA - not to read cause RAW
		res.put(Manga.MHA, new MangaState(Manga.MHA, 280, 281, Language.RAW));
		//ONE PIECE - more than 1 to read so Language not important
		res.put(Manga.ONE_PIECE, new MangaState(Manga.ONE_PIECE, 986, 987.5f, Language.SPOIL));
		return res;
	}

	public static Map<Manga, MangaState> getInitStatePostLastReadButSomeLeftToRead() {
		Map<Manga, MangaState> res = new HashMap<>();
		res.put(Manga.SOLO_LEVELING, new MangaState(Manga.SOLO_LEVELING, -1f, 105, Language.FRENCH));
		return res;
	}

}
