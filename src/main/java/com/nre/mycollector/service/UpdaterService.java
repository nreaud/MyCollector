package com.nre.mycollector.service;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;
import com.nre.mycollector.service.mapper.StateMapper;
import com.nre.mycollector.service.parser.MangaWebSiteParser;

public class UpdaterService {

	private static final Logger LOGGER = LogManager.getLogger(UpdaterService.class.getName());
	private final String urlMangaWebSite;
	private final String pathMyCurrentState;
	private final MangaWebSiteParser parser;

	public UpdaterService(final String urlMangaWebSite, final String pathMyCurrentState,
	    final MangaWebSiteParser parser) {
		this.urlMangaWebSite = urlMangaWebSite;
		this.pathMyCurrentState = pathMyCurrentState;
		this.parser = parser;
	}

	public void update() throws IOException {
		String htmlContent = HttpService.getContent(this.urlMangaWebSite);

		Map<Manga, Release> mapLatestReleases = parser.parse(htmlContent);

		Map<Manga, MangaState> latestStates = StateMapper.mapFromReleases(mapLatestReleases);

		LOGGER.info("test log info");
		if (!LOGGER.isDebugEnabled()) {
			LOGGER.info("why disabled?");
		} else {
			LOGGER.info("Why enabled?!!!");
		}
		LOGGER.info("=== Web site current state ===");
		latestStates.entrySet().stream().peek(entry -> LOGGER.info(entry.toString())).collect(Collectors.toList());

		Map<Manga, MangaState> myCurrentStates = StateWriterService.readCurrentStateSorted(this.pathMyCurrentState);

		LOGGER.info("=== My current state ===");
		myCurrentStates.entrySet().stream().peek(entry -> LOGGER.info(entry.toString())).collect(Collectors.toList());

		Map<Manga, MangaState> updates = Controller.getUpdates(myCurrentStates, latestStates);
		if (!updates.isEmpty()) {
			myCurrentStates = Controller.updateSorted(myCurrentStates, updates);

			LOGGER.info("=== My current state after update ===");
			myCurrentStates.entrySet().stream().peek(entry -> LOGGER.info(entry.toString())).collect(Collectors.toList());

			StateWriterService.writeCurrentState(myCurrentStates, pathMyCurrentState);
		}

	}

}
