package com.nre.mycollector.service;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;
import com.nre.mycollector.model.SortingMangas;
import com.nre.mycollector.service.mapper.StateMapper;
import com.nre.mycollector.service.parser.MangaWebSiteParser;
import com.nre.mycollector.utils.MangaStateUtils;

public class UpdaterService {

	private static final Logger LOGGER = LogManager.getLogger(UpdaterService.class.getName());
	private final String urlMangaWebSite;
	private final String pathCurrentStateWebSite;
	private final String pathMyCurrentState;
	private final MangaWebSiteParser parser;
	private HttpService httpService;

	public UpdaterService(final String urlMangaWebSite, final String pathCurrentStateWebSite,
	    final String pathMyCurrentState, final MangaWebSiteParser parser, HttpService httpService) {
		this.urlMangaWebSite = urlMangaWebSite;
		this.pathCurrentStateWebSite = pathCurrentStateWebSite;
		this.pathMyCurrentState = pathMyCurrentState;
		this.parser = parser;
		this.httpService = httpService;
	}

	public void update() throws IOException {
		String htmlContent = httpService.getContent(this.urlMangaWebSite);

		Map<Manga, Release> mapLatestReleases = parser.parse(htmlContent);

		//TODO log debug
		LOGGER.info("=== Web site latest state ===");
		MangaStateUtils.printMapReleases(mapLatestReleases, LOGGER);

		Map<Manga, Release> currentStateWebSite = StateFileService.readWebSiteStateSorted(pathCurrentStateWebSite);
		LOGGER.info("=== Web site current state");
		MangaStateUtils.printMapReleases(currentStateWebSite, LOGGER);

		Map<Manga, Release> updatesWebSite = MangaStateUtils.getUpdatesWebSite(currentStateWebSite, mapLatestReleases);
		if (!updatesWebSite.isEmpty()) {
			//TODO stay info
			LOGGER.info("=== Updates web site ===");
			MangaStateUtils.printMapReleases(updatesWebSite, LOGGER);

			currentStateWebSite = MangaStateUtils.updateWebSiteSorted(currentStateWebSite, updatesWebSite);
			LOGGER.info("=== Web site current state after updated ===");
			MangaStateUtils.printMapReleases(currentStateWebSite, LOGGER);

			StateFileService.writeWebSiteState(currentStateWebSite, pathCurrentStateWebSite);

			Map<Manga, MangaState> myCurrentStates = StateFileService.readCurrentState(this.pathMyCurrentState,
			    SortingMangas.ALPHABETIC);
			LOGGER.info("=== My current state ===");
			MangaStateUtils.print(myCurrentStates, LOGGER);

			Map<Manga, MangaState> updatesWebSiteMangaStates = StateMapper.mapFromReleases(updatesWebSite);

			Map<Manga, MangaState> updates = MangaStateUtils.getUpdates(myCurrentStates, updatesWebSiteMangaStates);
			if (!updates.isEmpty()) {
				//TODO stay info
				LOGGER.info("=== Updates current state ===");
				MangaStateUtils.print(updates, LOGGER);

				myCurrentStates = MangaStateUtils.updateSorted(myCurrentStates, updates);

				LOGGER.info("=== My current state after update ===");
				MangaStateUtils.print(myCurrentStates, LOGGER);

				StateFileService.writeCurrentState(myCurrentStates, pathMyCurrentState);
			}
		}

	}

}
