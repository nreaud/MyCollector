package com.nre.mycollector.service;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;
import com.nre.mycollector.service.mapper.StateMapper;
import com.nre.mycollector.service.parser.MangaWebSiteParser;
import com.nre.mycollector.utils.MangaStateUtils;

public class UpdaterService {

	private static final Logger LOGGER = LogManager.getLogger(UpdaterService.class.getName());
	private final String urlMangaWebSite;
	private final String pathCurrentStateWebSite;
	private final String pathMyCurrentState;
	private final MangaWebSiteParser parser;

	public UpdaterService(final String urlMangaWebSite, final String pathCurrentStateWebSite,
	    final String pathMyCurrentState, final MangaWebSiteParser parser) {
		this.urlMangaWebSite = urlMangaWebSite;
		this.pathCurrentStateWebSite = pathCurrentStateWebSite;
		this.pathMyCurrentState = pathMyCurrentState;
		this.parser = parser;
	}

	public void update() throws IOException {
		String htmlContent = HttpService.getContent(this.urlMangaWebSite);

		Map<Manga, Release> mapLatestReleases = parser.parse(htmlContent);
		Map<Manga, MangaState> latestStates = StateMapper.mapFromReleases(mapLatestReleases);

		//TODO log debug
		LOGGER.info("=== Web site latest state ===");
		MangaStateUtils.print(latestStates, LOGGER);

		//TODO only Release and not MangaState -> updatesWebSite Release, updates mangastate
		Map<Manga, MangaState> currentStateWebSite = StateFileService.readCurrentStateSorted(pathCurrentStateWebSite);
		LOGGER.info("=== Web site current state");
		MangaStateUtils.print(currentStateWebSite, LOGGER);

		Map<Manga, MangaState> updatesWebSite = Controller.getUpdates(currentStateWebSite, latestStates);
		if (!updatesWebSite.isEmpty()) {
			//TODO stay info
			LOGGER.info("=== Updates web site ===");
			MangaStateUtils.print(updatesWebSite, LOGGER);

			currentStateWebSite = Controller.updateSorted(currentStateWebSite, updatesWebSite);
			LOGGER.info("=== Web site current state after updated ===");
			MangaStateUtils.print(currentStateWebSite, LOGGER);

			StateFileService.writeCurrentState(currentStateWebSite, pathCurrentStateWebSite);

			Map<Manga, MangaState> myCurrentStates = StateFileService.readCurrentStateSorted(this.pathMyCurrentState);
			LOGGER.info("=== My current state ===");
			MangaStateUtils.print(myCurrentStates, LOGGER);

			Map<Manga, MangaState> updates = Controller.getUpdates(myCurrentStates, updatesWebSite);
			if (!updates.isEmpty()) {
				//TODO stay info
				LOGGER.info("=== Updates current state ===");
				MangaStateUtils.print(updates, LOGGER);

				myCurrentStates = Controller.updateSorted(myCurrentStates, updates);

				LOGGER.info("=== My current state after update ===");
				MangaStateUtils.print(myCurrentStates, LOGGER);

				StateFileService.writeCurrentState(myCurrentStates, pathMyCurrentState);
			}
		}

	}

}
