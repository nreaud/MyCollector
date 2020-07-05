package main.service.mapper;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import main.model.Manga;
import main.model.MangaState;
import main.model.Release;

/**
 * Map objet MangaState
 * 
 * @author nicol
 *
 */
public class StateMapper {
	public static Map<Manga, Release> mapToReleases(
			Map<Manga, MangaState> state) {
		return state.entrySet().stream().map(entry -> {
			MangaState mangaState = entry.getValue();
			return new Release(mangaState.getManga(), mangaState.getLastAvailable(),
					mangaState.getLastAvailableLanguage());
		}).collect(
				Collectors.toMap(release -> release.getManga(), release -> release));
	}

	public static Map<Manga, MangaState> mapFromReleases(
			Map<Manga, Release> releases) {
		BinaryOperator<MangaState> mergerDummy = (v1, v2) -> v1;

		return releases.entrySet().stream().map(entry -> {
			Release release = entry.getValue();
			return new MangaState(release.getManga(), (short) release.getNumber(),
					release.getLanguage());
		}).collect(Collectors.toMap(state -> state.getManga(), state -> state,
				mergerDummy, TreeMap::new)); // needTreeMap to stay sorted, merger dummy
																			// to call toMap function
	}
}
