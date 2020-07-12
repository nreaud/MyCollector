package com.nre.mycollector.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nre.mycollector.service.thread.UpdaterLauncherThread;

@Service
public class UpdaterLauncherService {

	private static final Logger LOGGER = LogManager.getLogger(UpdaterLauncherService.class);

	private UpdaterLauncherThread updaterLauncherThread;

	public UpdaterLauncherService(@Value("${path.mangastate.currentstate}") String myCurrentState,
	    @Value("${thread.minutesbeforenewupdate.lirescan}") float minutesBeforeNewUpdate,
	    @Value("${url.mangawebsite.lirescan}") String urlLireScan) {
		LOGGER.info(
		    "=== UpdaterLauncherComponent created with params pathCurrentState: {}, minutesSleeping: {} and urlLireScan: {} ===",
		    myCurrentState, minutesBeforeNewUpdate, urlLireScan);
		this.updaterLauncherThread = new UpdaterLauncherThread(myCurrentState, minutesBeforeNewUpdate, urlLireScan);
		this.updaterLauncherThread.start();
	}
}
