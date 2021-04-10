package com.nre.mycollector.service;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.MangaWebSite;
import com.nre.mycollector.model.Release;
import com.nre.mycollector.model.SortingMangas;
import com.nre.mycollector.service.mapper.StateMapper;
import com.nre.mycollector.service.parser.MangaWebSiteParser;
import com.nre.mycollector.utils.MangaStateUtils;

public class UpdaterService {

	private static final Logger LOGGER = LogManager.getLogger(UpdaterService.class.getName());
	private final MangaWebSite mangaWebSite;
	private final String pathMyCurrentState;
	private final MangaWebSiteParser parser;
	private HttpService httpService;

	public UpdaterService(final MangaWebSite mangaWebSite, final String pathMyCurrentState,
	    final MangaWebSiteParser parser, HttpService httpService) {
		this.mangaWebSite = mangaWebSite;
		this.pathMyCurrentState = pathMyCurrentState;
		this.parser = parser;
		this.httpService = httpService;
	}

	public void update() throws IOException {
		String htmlContent = httpService.getContent(this.mangaWebSite.getUrl());

		Map<Manga, Release> mapLatestReleases = parser.parse(htmlContent);

		//TODO log debug
		LOGGER.info("=== {} latest state ===", this.mangaWebSite.getName());
		MangaStateUtils.printMapReleases(mapLatestReleases, LOGGER);

		Map<Manga, Release> currentStateWebSite = StateFileService.readWebSiteStateSorted(this.mangaWebSite.getPathState());
		LOGGER.info("=== {} current state", this.mangaWebSite.getName());
		MangaStateUtils.printMapReleases(currentStateWebSite, LOGGER);

		Map<Manga, Release> updatesWebSite = MangaStateUtils.getUpdatesWebSite(currentStateWebSite, mapLatestReleases);
		if (!updatesWebSite.isEmpty()) {
			//TODO stay info
			LOGGER.info("=== Updates {} ===", this.mangaWebSite.getName());
			MangaStateUtils.printMapReleases(updatesWebSite, LOGGER);

			currentStateWebSite = MangaStateUtils.updateWebSiteSorted(currentStateWebSite, updatesWebSite);
			LOGGER.info("=== {} current state after updated ===", this.mangaWebSite.getName());
			MangaStateUtils.printMapReleases(currentStateWebSite, LOGGER);

			StateFileService.writeWebSiteState(currentStateWebSite, this.mangaWebSite.getPathState());

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
