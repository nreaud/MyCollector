package com.nre.mycollector.service.mapper;

import java.util.HashMap;
import java.util.Map;

import com.nre.mycollector.model.Manga;

public class SimpleMangaNameMapper {
	//TODO replace by intelligent one (guess from names)

	private static Map<String, Manga> mapConfWithEnum;

	private SimpleMangaNameMapper() {
		//hidden
	}

	static {
		mapConfWithEnum = new HashMap<>();
		mapConfWithEnum.put("ajin", Manga.AJIN);
		mapConfWithEnum.put("attackontitan", Manga.ATTACK_ON_TITAN);
		mapConfWithEnum.put("blackclover", Manga.BLACK_CLOVER);
		mapConfWithEnum.put("boruto", Manga.BORUTO);
		mapConfWithEnum.put("chainsawman", Manga.CHAINSAW_MAN);
		mapConfWithEnum.put("enigma", Manga.ENIGMA);
		mapConfWithEnum.put("fairtytail100yearsquest", Manga.FAIRY_TAIL_100_Y);
		mapConfWithEnum.put("jujutsukaisen", Manga.JUJUTSU_KAISEN);
		mapConfWithEnum.put("myheroacademia", Manga.MHA);
		mapConfWithEnum.put("onepiece", Manga.ONE_PIECE);
		mapConfWithEnum.put("onepunchman", Manga.ONE_PUNCH_MAN);
		mapConfWithEnum.put("onepunchmanone", Manga.ONE_PUNCH_MAN_ONE);
		mapConfWithEnum.put("platiniumend", Manga.PLATINIUM_END);
		mapConfWithEnum.put("sololeveling", Manga.SOLO_LEVELING);
		mapConfWithEnum.put("thepromisedneverland", Manga.THE_PROMISED_NEVERLAND);
		mapConfWithEnum.put("tokyorevengers", Manga.TOKYO_REVENGERS);
		mapConfWithEnum.put("worldtrigger", Manga.WORLD_TRIGGER);
	}

	public static Map<String, Manga> getMapConfWithEnum() {
		return mapConfWithEnum;
	}

	public static Manga getMangaByConfName(String name) {
		return mapConfWithEnum.get(name);
	}
}
