package com.nre.mycollector.service.parser;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Scanner for 600 words
 * 
 * @author nicol
 *
 */
//TODO - deprecated
public class SixCentsMotsParser {

	private static final List<String> BLACK_LIST = Arrays.asList("MERDE", "PUTAIN");

	private SixCentsMotsParser() {
		//hidden
	}

	public static Set<String> parse(String htmlContent) {
		Document doc = Jsoup.parse(htmlContent);
		Elements elements = doc.getElementsByClass("texte entry-content");

		Set<String> words = elements.get(0).children().stream().filter(item -> item.tagName().equals("table")) // gettings 3 tables
		    // (Adj, Noms Communs et
		    // Verbes)
		    .map(table -> table.child(0).children()) // gettings tableRow under
		    // <table> et <tbody>
		    .flatMap(elementsTable -> elementsTable.stream()) // gettings
		    // List<Element>
		    // containing words
		    .flatMap(tableRow -> tableRow.children().stream()) // gettins tables
		    // elements
		    .filter(tableItem -> tableItem.children().isEmpty()) // filtering
		    // middle of the
		    // table
		    .map(tableItem -> tableItem.child(0)) // getting under <td>
		    .filter(tableItem -> tableItem.tagName().equals("strong"))// filtering
		    // to get
		    // words
		    .map(wordContainer -> wordContainer.text()) // getting text under strong
		    .map(word -> unaccent(word)).map(word -> word.toUpperCase()).filter(word -> removeBlackListedWords(word))
		    .peek(item -> System.out.println("====== Element ======\r\n" + item)).collect(Collectors.toSet());

		return words;
	}

	private static boolean removeBlackListedWords(String word) {
		Optional<Boolean> isBlackListed = BLACK_LIST.stream().filter(blackListedWord -> blackListedWord.equals(word))
		    .map(blackListedWord -> true).findAny();
		return !isBlackListed.isPresent();

	}

	public static String unaccent(String src) {
		return Normalizer.normalize(src, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
}
