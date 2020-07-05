package main.service.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import main.model.Language;
import main.model.Manga;

/**
 * Mapper for lirescan.me, associate manga names with site
 * 
 * @author nicol
 *
 */
public class LireScanMapper {
	private static final Map<String, Manga> mangaNames;
	private static final Map<String, Language> languages;
	static {
		Map<String, Manga> mapMangaNames = new HashMap<>();
		mapMangaNames.put("Ajin", Manga.AJIN);
		mapMangaNames.put("Black Clover", Manga.BLACK_CLOVER);
		mapMangaNames.put("Boruto", Manga.BORUTO);
		mapMangaNames.put("Fairy Tail 100 Years Quest", Manga.FAIRY_TAIL_100_Y);
		mapMangaNames.put("My Hero Academia", Manga.MHA);
		mapMangaNames.put("One Piece", Manga.ONE_PIECE);
		mapMangaNames.put("One Punch Man", Manga.ONE_PUNCH_MAN);
		mapMangaNames.put("Shingeki No Kyojin", Manga.SHINGEKI);
		mapMangaNames.put("World Trigger", Manga.WORLD_TRIGGER);
		mangaNames = Collections.unmodifiableMap(mapMangaNames);

		Map<String, Language> mapLanguage = new HashMap<>();
		mapLanguage.put("RAW", Language.RAW);
		mapLanguage.put("SPOIL", Language.SPOIL);
		mapLanguage.put("VUS", Language.ENGLISH);
		languages = Collections.unmodifiableMap(mapLanguage);
	}

	public static Manga getMangaByName(String name) {
		Manga manga = Manga.UNKNOWN;
		if (name != null && mangaNames.containsKey(name)) {
			manga = mangaNames.get(name);
		}
		return manga;
	}

	public static Language getLanguageByName(String name) {
		Language language = Language.FRENCH;
		if (name != null && languages.containsKey(name)) {
			language = languages.get(name);
		}
		return language;
	}

}
