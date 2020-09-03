package com.nre.mycollector.service.conf;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "path")
public class MangaImgsConf {

	private String mangaImgUrl;
	private Map<String, String> mangaImgs;

	public Map<String, String> getMangaImgs() {
		return mangaImgs;
	}

	public void setMangaImgs(Map<String, String> mangaImgs) {
		this.mangaImgs = mangaImgs;
	}

	public String getMangaImgUrl() {
		return mangaImgUrl;
	}

	public void setMangaImgUrl(String mangaImgUrl) {
		this.mangaImgUrl = mangaImgUrl;
	}

}
