package com.nre.mycollector.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaDTO;
import com.nre.mycollector.service.conf.MangaImgsConf;
import com.nre.mycollector.service.mapper.SimpleMangaNameMapper;
import com.nre.mycollector.utils.URLBuilder;

@Service
public class MangaService {

	@Autowired
	MangaImgsConf mangaimgs;

	public List<MangaDTO> getMangasDTO() {
		String urlMangaImgs = URLBuilder.addSlashAtEndIfNeeded(mangaimgs.getMangaImgUrl());
		return mangaimgs.getMangaImgs().entrySet().stream().map(entry -> {
			String mangaName = entry.getKey();
			String img = entry.getValue();
			Manga manga = SimpleMangaNameMapper.getMangaByConfName(mangaName);
			return new MangaDTO(urlMangaImgs + img, manga, manga.getName());
		}).collect(Collectors.toList());
	}

}
