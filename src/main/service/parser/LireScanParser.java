package main.service.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.model.Manga;
import main.model.Release;
import main.service.collector.ReleasesCollector;
import main.service.mapper.LireScanMapper;

/**
 * Scanner for lireScan.me
 * 
 * @author nicol
 *
 */
public class LireScanParser {
	private static Pattern releasePattern = Pattern
			.compile("Scan ([a-zA-Z0-9 -.]*) ([0-9.]*) VF ?([A-Z]*)");

	public static Map<Manga, Release> parse(String htmlContent) {
		Document doc = Jsoup.parse(htmlContent.toString());
		Element releasesContainer = doc.getElementById("releases");
		Elements releases = releasesContainer.getAllElements();

		return releases.stream().filter(release -> release.tagName().equals("li")) // The
																																								// releases
																																								// are
																																								// stored
																																								// under
																																								// <li>
																																								// tags
				.map(release -> release.text()) // The releases informations are stored
																				// like text
				.map(release -> mapToReleaseObject(release)) // Cf mapToReleaseObject
				.filter(release -> !release.getManga().equals(Manga.UNKNOWN)) // Manga
																																			// not in
																																			// my Enum
																																			// dont
																																			// intersest
																																			// me, so
																																			// filtered
				.collect(ReleasesCollector.getInstance()); // Cf ReleasesCollector

	}

	/**
	 * Translate release from text to object thx to releasePattern
	 * 
	 * @param releaseStr
	 * @return
	 */
	private static Release mapToReleaseObject(String releaseStr) {
		Matcher matcher = releasePattern.matcher(releaseStr);
		Release release = new Release();
		if (matcher.matches()) {
			String name = matcher.group(1);
			release.setManga(LireScanMapper.getMangaByName(name));

			String numberStr = matcher.group(2);
			release.setNumber(new Float(numberStr));

			String languageStr = matcher.group(3);
			release.setLanguage(LireScanMapper.getLanguageByName(languageStr));

		}
		return release;
	}
}
