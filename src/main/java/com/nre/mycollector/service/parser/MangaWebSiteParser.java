package service.parser;

import java.util.Map;

import model.Manga;
import model.Release;

public abstract class MangaWebSiteParser {

	public abstract Map<Manga, Release> parse(String htmlContent);

}
