package com.nre.mycollector.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.nre.mycollector.service.thread.UpdaterLauncherThread;

@Service
public class UpdaterLauncherService {

	private static final Logger LOGGER = LogManager.getLogger(UpdaterLauncherService.class);

	private UpdaterLauncherThread updaterLauncherThread;

	public UpdaterLauncherService() {
		LOGGER.info("=== UpdaterLauncherComponent created ===");
		this.updaterLauncherThread = new UpdaterLauncherThread();
		this.updaterLauncherThread.start();
	}
}
