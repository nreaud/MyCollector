package com.nre.mycollector.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaDTO;
import com.nre.mycollector.service.conf.MangaImgsConf;
import com.nre.mycollector.service.mapper.SimpleMangaNameMapper;
import com.nre.mycollector.utils.Pair;
import com.nre.mycollector.utils.URLBuilder;

@Service
public class MangaService {

	@Autowired
	MangaImgsConf mangaimgs;

	public Map<Manga, MangaDTO> getMangas() {
		String urlMangaImgs = URLBuilder.addSlashAtEndIfNeeded(mangaimgs.getMangaImgUrl());
		return mangaimgs.getMangaImgs().entrySet().stream().map(entry -> {
			String mangaName = entry.getKey();
			String img = entry.getValue();
			Manga manga = SimpleMangaNameMapper.getMangaByConfName(mangaName);
			return new Pair<>(manga, new MangaDTO(urlMangaImgs + img, manga.getName()));
		}).collect(Collectors.toMap(pair -> pair.one, pair -> pair.two));
	}

}
