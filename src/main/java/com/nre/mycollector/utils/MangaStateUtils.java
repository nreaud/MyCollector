package com.nre.mycollector.utils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.Language;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;
import com.nre.mycollector.model.SortingMangas;

public class MangaStateUtils {

	private MangaStateUtils() {
		//hidden
	}

	private static final Comparator<MangaState> ALPHABETIC_SORTER = Comparator.comparing(MangaState::getManga)
	    .thenComparing(MangaState::getLastAvailable).thenComparing(MangaState::getLastAvailableLanguage);

	public static final Function<MangaState, Integer> TO_READ_FCT_EXTRACTOR = mangaState -> {
		//TODO test 0.5
		int toRead = Math.round(mangaState.getLastAvailable() - mangaState.getLastRead());
		if (toRead == 1 && !Language.atLeast(mangaState.getLastAvailableLanguage(), Language.ENGLISH)) { //si 1 chapitre à lire, il faut au moins anglais
			toRead = 0;
		} else if (toRead >= 1) { //si plus d'un chapitre à lire, pas besoin de checker langue
			toRead = -1; //tout les "à lire" sont au même plan, (si 15 chapitres à lire pas plus important que si 1 seul)
		}
		return toRead;
	};

	private static final Comparator<MangaState> TO_READ_SORTER = Comparator.comparing(TO_READ_FCT_EXTRACTOR)
	    .thenComparing(ALPHABETIC_SORTER);

	/**
	 * Retourne la liste des manges à mettre à jour (peut être vide)
	 * 
	 * @param myCurrentStates
	 * @param newMangaStates
	 * @return une liste de maj ou vide
	 */
	public static Map<Manga, MangaState> getUpdates(Map<Manga, MangaState> myCurrentStates,
	    Map<Manga, MangaState> newMangaStates) {
		return newMangaStates.entrySet().stream().filter(entry -> {
			Manga manga = entry.getKey();
			MangaState newState = entry.getValue();
			MangaState currentState = myCurrentStates.get(manga);
			if (currentState == null) { // Might be the case with a new manga
				currentState = new MangaState(manga, (short) -1, Language.SPOIL);
			}
			return MangaState.moreRecent(currentState, newState);
		}).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	public static Map<Manga, Release> getUpdatesWebSite(Map<Manga, Release> myCurrentStates,
	    Map<Manga, Release> newMangaStates) {
		return newMangaStates.entrySet().stream().filter(entry -> {
			Manga manga = entry.getKey();
			Release newState = entry.getValue();
			Release currentState = myCurrentStates.get(manga);
			if (currentState == null) { // Might be the case with a new manga
				currentState = new Release(manga, (short) -1, Language.SPOIL);
			}
			return Release.moreRecent(currentState, newState);
		}).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	public static void print(Map<Manga, MangaState> state, Logger logger) {
		//TODO log debug + inject loglevel into method
		state.entrySet().stream().peek(entry -> logger.info(entry.toString())).collect(Collectors.toList());
	}

	public static void printMapReleases(Map<Manga, Release> state, Logger logger) {
		//TODO log debug + inject loglevel into method
		state.entrySet().stream().peek(entry -> logger.info(entry.toString())).collect(Collectors.toList());
	}

	//TODO other sorting
	public static Map<Manga, MangaState> sort(SortingMangas sortingStrategie, Map<Manga, MangaState> map) {
		Map<Manga, MangaState> res;
		switch (sortingStrategie) {
		case ALPHABETIC:
			res = map.entrySet().stream().sorted(Map.Entry.comparingByValue(ALPHABETIC_SORTER)).collect(Collectors
			    .toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			break;
		case TO_READ:
			res = map.entrySet().stream().sorted(Map.Entry.comparingByValue(TO_READ_SORTER)).collect(Collectors
			    .toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			break;
		case NONE:
		default:
			res = map;
			break;
		}

		return res;
	}

	/**
	 * À appeler si getUpdates à renvoyer une liste non vide, met à jour
	 * currentState
	 * 
	 * @param myCurrentStates
	 * @param updates
	 * @return current state mis à jour
	 */
	public static Map<Manga, MangaState> updateSorted(Map<Manga, MangaState> myCurrentStates,
	    Map<Manga, MangaState> updates) {
		BinaryOperator<MangaState> mergerDummy = (v1, v2) -> v1;
		// Firstly we update the known mangaStates
		Map<Manga, MangaState> res = myCurrentStates.entrySet().stream().map(entry -> {
			MangaState currentState = entry.getValue();
			if (updates.containsKey(entry.getKey())) {
				MangaState newState = updates.get(entry.getKey());
				currentState.setLastAvailable(newState.getLastAvailable());
				currentState.setLastAvailableLanguage(newState.getLastAvailableLanguage());
			}
			return currentState;
		}).sorted().collect(Collectors.toMap(MangaState::getManga, mangaState -> mangaState, mergerDummy, TreeMap::new));

		// Then we check if there are new mangas (new interest) to update res <=> no
		// need to set json file on hand
		Map<Manga, MangaState> newMangas = updates.entrySet().stream()
		    .filter(entry -> !myCurrentStates.containsKey(entry.getKey()))
		    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		if (!newMangas.isEmpty()) {
			for (Entry<Manga, MangaState> newEntry : newMangas.entrySet()) {
				res.put(newEntry.getKey(), newEntry.getValue());
			}
		}
		return res;
	}

	public static Map<Manga, Release> updateWebSiteSorted(Map<Manga, Release> currentStateWebSite,
	    Map<Manga, Release> updatesWebSite) {
		BinaryOperator<Release> mergerDummy = (v1, v2) -> v1;
		// Firstly we update the known mangaStates
		Map<Manga, Release> res = currentStateWebSite.entrySet().stream().map(entry -> {
			Release currentState = entry.getValue();
			if (updatesWebSite.containsKey(entry.getKey())) {
				Release newState = updatesWebSite.get(entry.getKey());
				currentState.setNumber(newState.getNumber());
				currentState.setLanguage(newState.getLanguage());
			}
			return currentState;
		}).sorted().collect(Collectors.toMap(Release::getManga, release -> release, mergerDummy, TreeMap::new));

		// Then we check if there are new mangas (new interest) to update res -> no
		// need to set json file on hand
		Map<Manga, Release> newMangas = updatesWebSite.entrySet().stream()
		    .filter(entry -> !currentStateWebSite.containsKey(entry.getKey()))
		    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		if (!newMangas.isEmpty()) {
			for (Entry<Manga, Release> newEntry : newMangas.entrySet()) {
				res.put(newEntry.getKey(), newEntry.getValue());
			}
		}
		return res;
	}

}
