package com.nre.mycollector.service.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.service.parser.LireScanParser;

public class UpdaterLauncherThread extends Thread {

	private final Logger logger = LogManager.getLogger(UpdaterLauncherThread.class);

	private final long millisecsSleeping;
	private final float minutesBeforeNewUpdate;
	private final String pathCurrentStateWebSite;
	private final String pathMyCurrentState;
	private final String urlLirescan;

	public UpdaterLauncherThread(String pathCurrentStateWebSite, String pathMyCurrentState, float minutesBeforeNewUpdate,
	    String urlLirescan) {
		this.millisecsSleeping = (long) (minutesBeforeNewUpdate * 60 * 1000);
		this.minutesBeforeNewUpdate = minutesBeforeNewUpdate;
		this.pathCurrentStateWebSite = pathCurrentStateWebSite;
		this.pathMyCurrentState = pathMyCurrentState;
		this.urlLirescan = urlLirescan;
	}

	@Override
	public void run() {
		logger.info("Updater launcher running");
		do {
			try {
				Thread updaterThread = new UpdaterThread(urlLirescan, pathCurrentStateWebSite, pathMyCurrentState,
				    new LireScanParser());
				updaterThread.start();
				Thread.sleep(millisecsSleeping); //sleep self
			} catch (InterruptedException e) {
				logger.debug(e.toString());
				Thread.currentThread().interrupt();
				UpdaterLauncherThread newInstance = new UpdaterLauncherThread(pathCurrentStateWebSite, pathMyCurrentState,
				    minutesBeforeNewUpdate, urlLirescan);
				newInstance.start();
			}
		} while (true);
	}

}
