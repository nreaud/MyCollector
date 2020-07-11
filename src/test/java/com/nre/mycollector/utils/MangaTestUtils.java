package com.nre.mycollector.utils;

import java.util.HashMap;
import java.util.Map;

import com.nre.mycollector.model.Language;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;

public class MangaTestUtils {

	public static Map<Manga, MangaState> getInitState() {
		Map<Manga, MangaState> res = new HashMap<>();
		//AJIN - test update language
		res.put(Manga.AJIN, new MangaState(Manga.AJIN, (short) 77, Language.ENGLISH));
		//BLACK CLOVER - test update number
		res.put(Manga.BLACK_CLOVER, new MangaState(Manga.BLACK_CLOVER, (short) 251, (short) 252, Language.FRENCH));
		return res;
	}

}
