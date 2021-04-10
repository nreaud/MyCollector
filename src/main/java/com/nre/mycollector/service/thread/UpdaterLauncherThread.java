package com.nre.mycollector.service.thread;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.MangaWebSite;
import com.nre.mycollector.service.parser.LireScanParser;
import com.nre.mycollector.service.parser.MangaWebSiteParser;
import com.nre.mycollector.service.parser.SmartMangaWebSiteParser;

public class UpdaterLauncherThread extends Thread {

	private final Logger logger = LogManager.getLogger(UpdaterLauncherThread.class);

	private final long millisecsSleeping;
	private final float minutesBeforeNewUpdate;
	private final String pathMyCurrentState;
	private final List<MangaWebSite> mangasWebsites;

	public UpdaterLauncherThread(List<MangaWebSite> mangasWebSites, String pathMyCurrentState,
	    float minutesBeforeNewUpdate) {
		this.millisecsSleeping = (long) (minutesBeforeNewUpdate * 60 * 1000);
		this.minutesBeforeNewUpdate = minutesBeforeNewUpdate;
		this.mangasWebsites = mangasWebSites;
		this.pathMyCurrentState = pathMyCurrentState;
	}

	@Override
	public void run() {
		logger.info("Updater launcher running");
		do {
			try {
				for (MangaWebSite mangaWebSite : mangasWebsites) {
					MangaWebSiteParser parser;
					if (mangaWebSite.equals("lirescan")) {
						parser = new LireScanParser();
					} else {
						parser = new SmartMangaWebSiteParser(mangaWebSite.getPathState());
					}
					Thread updaterThread = new UpdaterThread(mangaWebSite, pathMyCurrentState, parser);
					updaterThread.start();
				}
				Thread.sleep(millisecsSleeping); //sleep self
			} catch (InterruptedException e) {
				logger.debug(e.toString());
				Thread.currentThread().interrupt();
				UpdaterLauncherThread newInstance = new UpdaterLauncherThread(mangasWebsites, pathMyCurrentState,
				    minutesBeforeNewUpdate);
				newInstance.start();
			}
		} while (true);
	}

}
