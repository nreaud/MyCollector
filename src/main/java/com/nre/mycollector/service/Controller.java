package com.nre.mycollector.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nre.mycollector.model.Language;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;

// TODO Move into MangaStateUtils (quid ReleaseUtils)
@Service
public class Controller {

	private Controller() {
		//hidden
	}

	//TODO moreRecent functions inside respectively MangaState and Release
	private static boolean moreRecent(MangaState currentState, MangaState newState) {
		return (newState.getLastAvailable() > currentState.getLastAvailable())
		    || ((newState.getLastAvailable().equals(currentState.getLastAvailable()))
		        && (Language.moreRecent(newState.getLastAvailableLanguage(), currentState.getLastAvailableLanguage())));
	}

	private static boolean moreRecent(Release currentState, Release newState) {
		return (newState.getNumber() > currentState.getNumber())
		    || (Language.moreRecent(newState.getLanguage(), currentState.getLanguage()));
	}

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
			return moreRecent(currentState, newState);
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
			return moreRecent(currentState, newState);
		}).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
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

		// Then we check if there are new mangas (new interest) to update res -> no
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
