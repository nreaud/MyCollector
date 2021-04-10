package com.nre.mycollector.service.parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nre.mycollector.model.Language;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.Release;
import com.nre.mycollector.service.StateFileService;
import com.nre.mycollector.utils.Pair;
import com.nre.mycollector.utils.StringUtils;

public class SmartMangaWebSiteParser extends MangaWebSiteParser {

	private static final Pattern CHAPTER_NUMBER_IN_LINK = Pattern.compile("([0-9][0-9,.]*)");
	//NOT vf cause causes troubles, sometimes present for no reason
	private static final Pattern CHAPTER_LANGUAGE_IN_LINK = Pattern.compile("(va|english|vus|raw|spoil)",
	    Pattern.CASE_INSENSITIVE);

	private final String webSiteStateLocation;

	public SmartMangaWebSiteParser(String webSiteStateLocation) {
		this.webSiteStateLocation = webSiteStateLocation;
	}

	@Override
	public Map<Manga, Release> parse(String htmlContent) {
		Map<Manga, Release> res = new EnumMap<>(Manga.class);
		Document doc = Jsoup.parse(htmlContent);
		Map<Manga, Release> mangasWebSiteState;
		try {
			mangasWebSiteState = StateFileService.readWebSiteState(webSiteStateLocation);
			//Chapters are usually under <a href>
			Elements links = doc.select("a[href]"); // a with href
			Map<Manga, String> mapMangaInfos = links.stream().map(this::extractStringsFromLink)
			    .collect(Collectors.toMap(this::extractMangaFromLink, linkInfo -> linkInfo, (linkInfo1, linkInfo2) -> {
				    String linkInfos = linkInfo1;
				    if (!linkInfo1.equals(linkInfo2)) {
					    linkInfos = linkInfo1 + linkInfo2;
				    }
				    return linkInfos;
			    }));
			res = mapMangaInfos.entrySet().stream().filter(entry -> entry.getKey() != Manga.UNKNOWN)
			    .map(entry -> this.mapValueToRelease(entry, retrieveLastChapter(entry.getKey(), mangasWebSiteState)))
			    .filter(pair -> pair.one != Manga.UNKNOWN).collect(Collectors.toMap(pair -> pair.one, pair -> pair.two));
			res.entrySet().stream().peek(item -> System.out.println(item)).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private Optional<Pair<Float, Language>> extractChapterAndLanguageFromLink(String link, float lastChapter) {
		Matcher matcherChapterNber = CHAPTER_NUMBER_IN_LINK.matcher(link);
		Optional<Pair<Float, Language>> res = Optional.empty();
		float chapterNber = -1;
		Language language = Language.FRENCH;
		while (matcherChapterNber.find()) {//We can find wrong numbers due to derived series (mha teams up)
			float number = Float.parseFloat(matcherChapterNber.group(1));
			if (number > lastChapter && number > chapterNber) {
				chapterNber = number;
			}
		}
		if (chapterNber > -1) {
			//otherwise no need to check language
			Matcher matcherLanguage = CHAPTER_LANGUAGE_IN_LINK.matcher(link);
			if (matcherLanguage.find()) {
				String languageStr = matcherLanguage.group(1).toUpperCase();
				language = Language.convertFromString(languageStr);
			}
			Pair<Float, Language> pair = new Pair<>(chapterNber, language);
			res = Optional.of(pair);
		}
		return res;
	}

	private Manga extractMangaFromLink(String linkInfos) {
		return Arrays.stream(Manga.values())
		    .filter(manga -> StringUtils.textContainedInAtLeastOne(manga.getNames(), linkInfos)).findFirst()
		    .orElse(Manga.UNKNOWN);

	}

	private String extractStringsFromLink(Element link) {
		return link.attr("href") + link.text();
	}

	private Pair<Manga, Release> mapValueToRelease(Entry<Manga, String> entry, float lastChapter) {
		Pair<Manga, Release> res;
		Release release;
		Manga manga = entry.getKey();
		//First we need to remove chapter name from infos in order to not confuse the chapter number (ex: FairyTail100YearsQuest)
		String cleansedInfos = this.removeMangaNameFromInfos(entry.getValue(), manga.getNames());
		Optional<Pair<Float, Language>> opt = extractChapterAndLanguageFromLink(cleansedInfos, lastChapter);
		if (opt.isPresent()) {
			Pair<Float, Language> pair = opt.get();
			release = new Release(manga, pair.one, pair.two);
			res = new Pair<>(manga, release);
		} else {
			res = new Pair<>(Manga.UNKNOWN, null);
		}
		return res;
	}

	private String removeMangaNameFromInfos(String link, String[] names) {
		String linkClean = link.replaceAll("[/-]", " ");
		for (String name : names) {
			Pattern patternRemovingName = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
			Matcher matcher = patternRemovingName.matcher(linkClean);
			linkClean = matcher.replaceAll("");
		}
		return linkClean;
	}

	private float retrieveLastChapter(Manga manga, Map<Manga, Release> mangasWebSiteState) {
		float res = -1;
		if (mangasWebSiteState.containsKey(manga)) {
			res = mangasWebSiteState.get(manga).getNumber();
		}
		return res;
	}

}
