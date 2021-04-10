package com.nre.mycollector.service.thread;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.MangaWebSite;
import com.nre.mycollector.service.HttpService;
import com.nre.mycollector.service.UpdaterService;
import com.nre.mycollector.service.parser.MangaWebSiteParser;

public class UpdaterThread extends Thread {

	private final MangaWebSite mangaWebSite;
	private final String pathMyCurrentState;
	private final MangaWebSiteParser parser;

	private static final Logger logger = LogManager.getLogger(UpdaterThread.class);

	public UpdaterThread(final MangaWebSite mangaWebSite, final String pathMyCurrentState,
	    final MangaWebSiteParser parser) {
		this.mangaWebSite = mangaWebSite;
		this.pathMyCurrentState = pathMyCurrentState;
		this.parser = parser;
	}

	@Override
	public void run() {
		logger.debug("Updater thread launched");
		if (!logger.isDebugEnabled()) {
			logger.info("I thought it would be enabled");
		} else {
			logger.info("Indeed enabled");
		}
		try {
			UpdaterService updaterService = new UpdaterService(mangaWebSite, pathMyCurrentState, parser, new HttpService());
			updaterService.update();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

}
