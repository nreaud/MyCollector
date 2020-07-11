package com.nre.mycollector.service.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nre.mycollector.service.parser.LireScanParser;

@Component
public class UpdaterLauncherThread extends Thread {

	private final Logger logger = LogManager.getLogger(UpdaterLauncherThread.class);

	private static final int SECONDS_SLEEPING = 30;
	private static final String URL_LIRESCAN = "https://www.lirescan.me/";
	private static final String PATH_MY_CURRENT_STATE = "src/main/resources/mangaState.json";

	@Override
	public void run() {
		logger.info("Updater launcher running");
		do {
			try {
				Thread updaterThread = new UpdaterThread(URL_LIRESCAN, PATH_MY_CURRENT_STATE, new LireScanParser());
				updaterThread.start();
				Thread.sleep(SECONDS_SLEEPING * 1000L); //sleep self
			} catch (InterruptedException e) {
				logger.debug(e.toString());
				Thread.currentThread().interrupt();
				UpdaterLauncherThread newInstance = new UpdaterLauncherThread();
				newInstance.start();
			}
		} while (true);
	}

}
