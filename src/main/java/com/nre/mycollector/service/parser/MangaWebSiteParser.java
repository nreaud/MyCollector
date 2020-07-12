package com.nre.mycollector.service.parser;

import java.util.Map;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.Release;

// TODO interface? interface vs heritance?
public abstract class MangaWebSiteParser {

	public abstract Map<Manga, Release> parse(String htmlContent);

}
