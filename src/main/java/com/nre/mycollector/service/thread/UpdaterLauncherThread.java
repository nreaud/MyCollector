package com.nre.mycollector.service.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nre.mycollector.service.parser.LireScanParser;

public class UpdaterLauncherThread extends Thread {

	private final Logger logger = LogManager.getLogger(UpdaterLauncherThread.class);

	private final String pathMyCurrentState;
	private final long millisecsSleeping;
	private final float minutesBeforeNewUpdate;
	private final String urlLirescan;

	public UpdaterLauncherThread(String pathMyCurrentState, float minutesBeforeNewUpdate, String urlLirescan) {
		this.pathMyCurrentState = pathMyCurrentState;
		this.minutesBeforeNewUpdate = minutesBeforeNewUpdate;
		this.urlLirescan = urlLirescan;
		this.millisecsSleeping = (long) (minutesBeforeNewUpdate * 60 * 1000);
	}

	@Override
	public void run() {
		logger.info("Updater launcher running");
		do {
			try {
				Thread updaterThread = new UpdaterThread(urlLirescan, pathMyCurrentState, new LireScanParser());
				updaterThread.start();
				Thread.sleep(millisecsSleeping); //sleep self
			} catch (InterruptedException e) {
				logger.debug(e.toString());
				Thread.currentThread().interrupt();
				UpdaterLauncherThread newInstance = new UpdaterLauncherThread(pathMyCurrentState, minutesBeforeNewUpdate,
				    urlLirescan);
				newInstance.start();
			}
		} while (true);
	}

}
