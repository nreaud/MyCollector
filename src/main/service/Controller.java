package main.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import main.model.Language;
import main.model.Manga;
import main.model.MangaState;

public class Controller {

	private static boolean moreRecent(MangaState currentState,
			MangaState newState) {
		return (newState.getLastAvailable() > currentState.getLastAvailable())
				|| ((newState.getLastAvailable() == currentState.getLastAvailable())
						&& (Language.moreRecent(newState.getLastAvailableLanguage(),
								currentState.getLastAvailableLanguage())));
	}

	/**
	 * Retourne la liste des manges à mettre à jour (peut être vide)
	 * 
	 * @param myCurrentStates
	 * @param newMangaStates
	 * @return une liste de maj ou vide
	 */
	public static Map<Manga, MangaState> getUpdates(
			Map<Manga, MangaState> myCurrentStates,
			Map<Manga, MangaState> newMangaStates) {
		return newMangaStates.entrySet().stream().filter(entry -> {
			Manga manga = entry.getKey();
			MangaState newState = entry.getValue();
			MangaState currentState = myCurrentStates.get(manga);
			if (currentState == null) { // Might be the case with a new manga
				currentState = new MangaState(manga, (short) -1, Language.SPOIL);
			}
			return moreRecent(currentState, newState);
		}).collect(
				Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
	}

	/**
	 * À appeler si getUpdates à renvoyer une liste non vide, met à jour
	 * currentState
	 * 
	 * @param myCurrentStates
	 * @param updates
	 * @return current state mis à jour
	 */
	public static Map<Manga, MangaState> update(
			Map<Manga, MangaState> myCurrentStates, Map<Manga, MangaState> updates) {
		// Firstly we update the known mangaStates
		Map<Manga, MangaState> res = myCurrentStates.entrySet().stream()
				.map(entry -> {
					MangaState currentState = entry.getValue();
					if (updates.containsKey(entry.getKey())) {
						MangaState newState = updates.get(entry.getKey());
						currentState.setLastAvailable(newState.getLastAvailable());
						currentState
								.setLastAvailableLanguage(newState.getLastAvailableLanguage());
					}
					return currentState;
				}).collect(Collectors.toMap(mangaState -> mangaState.getManga(),
						mangaState -> mangaState));

		// Then we check if there are new mangas (new interest) to update res -> no
		// need to set json file on hand
		Map<Manga, MangaState> newMangas = updates.entrySet().stream()
				.filter(entry -> !myCurrentStates.containsKey(entry.getKey()))
				.collect(Collectors.toMap(entry -> entry.getKey(),
						entry -> entry.getValue()));

		if (!newMangas.isEmpty()) {
			for (Entry<Manga, MangaState> newEntry : newMangas.entrySet()) {
				res.put(newEntry.getKey(), newEntry.getValue());
			}
		}
		return res;
	}
}
