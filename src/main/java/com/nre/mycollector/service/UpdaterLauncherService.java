package com.nre.mycollector.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nre.mycollector.model.MangaWebSite;
import com.nre.mycollector.service.thread.UpdaterLauncherThread;

/**
 * This class is instanced by Spring and launch the updaterLauncher
 * 
 * @author nicol
 *
 */
@Service
public class UpdaterLauncherService {

	private static final Logger LOGGER = LogManager.getLogger(UpdaterLauncherService.class);

	private UpdaterLauncherThread updaterLauncherThread;

	public UpdaterLauncherService(@Value("${path.mangastate.lirescanstate}") String lirescanState,
	    @Value("${path.mangastate.lelscanstate}") String lelscanState,
	    @Value("${path.mangastate.currentstate}") String myCurrentState,
	    @Value("${thread.minutesbeforenewupdate.lirescan}") float minutesBeforeNewUpdate,
	    @Value("${url.mangawebsite.lirescan}") String urlLirescan,
	    @Value("${url.mangawebsite.lelscan}") String urlLelscan) {
		LOGGER.info(
		    "=== UpdaterLauncherComponent created with params pathCurrentState: {}, pathLirescanState: {}, "
		        + "pathLelscanState: {}, minutesSleeping: {}, urlLireScan: {}, urlLelscan: {} ===",
		    myCurrentState, lirescanState, lelscanState, minutesBeforeNewUpdate, urlLirescan, urlLelscan);
		List<MangaWebSite> mangaWebSites = new ArrayList<>();
		mangaWebSites.add(new MangaWebSite("lirescan", lirescanState, urlLirescan));
		mangaWebSites.add(new MangaWebSite("lelscan", lelscanState, urlLelscan));
		this.updaterLauncherThread = new UpdaterLauncherThread(mangaWebSites, myCurrentState, minutesBeforeNewUpdate);
		this.updaterLauncherThread.start();
	}
}
