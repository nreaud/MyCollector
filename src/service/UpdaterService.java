package service;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import model.Manga;
import model.MangaState;
import model.Release;
import service.mapper.StateMapper;
import service.parser.MangaWebSiteParser;

public class UpdaterService {
	private final String URL;
	private final String PATH_MY_CURRENT_STATE;
	private final MangaWebSiteParser PARSER;

	public UpdaterService(final String URL, final String PATH_MY_CURRENT_STATE, final MangaWebSiteParser PARSER) {
		this.URL = URL;
		this.PATH_MY_CURRENT_STATE = PATH_MY_CURRENT_STATE;
		this.PARSER = PARSER;
	}

	public void update() throws IOException {
		String htmlContent = HttpService.getContent(this.URL, true);

		Map<Manga, Release> mapLatestReleases = PARSER.parse(htmlContent);

		Map<Manga, MangaState> latestStates = StateMapper.mapFromReleases(mapLatestReleases);

		System.out.println("=== Web site current state ===");
		latestStates.entrySet().stream().peek(entry -> System.out.println(entry)).collect(Collectors.toList());

		Map<Manga, MangaState> myCurrentStates = StateWriterService.readCurrentStateSorted(this.PATH_MY_CURRENT_STATE);

		System.out.println("=== My current state ===");
		myCurrentStates.entrySet().stream().peek(entry -> System.out.println(entry)).collect(Collectors.toList());

		Map<Manga, MangaState> updates = Controller.getUpdates(myCurrentStates, latestStates);
		if (!updates.isEmpty()) {
			myCurrentStates = Controller.updateSorted(myCurrentStates, updates);

			System.out.println("=== My current state after update ===");
			myCurrentStates.entrySet().stream().peek(entry -> System.out.println(entry)).collect(Collectors.toList());

			StateWriterService.writeCurrentState(myCurrentStates, PATH_MY_CURRENT_STATE);
		}

	}

}
