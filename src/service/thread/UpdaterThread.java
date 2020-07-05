package service.thread;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.UpdaterService;
import service.parser.MangaWebSiteParser;

public class UpdaterThread extends Thread {

	private final String URL;
	private final String PATH_MY_CURRENT_STATE;
	private final MangaWebSiteParser PARSER;

	private static final Logger logger = LoggerFactory.getLogger(UpdaterThread.class);

	public UpdaterThread(final String URL, final String PATH_MY_CURRENT_STATE, final MangaWebSiteParser PARSER) {
		this.URL = URL;
		this.PATH_MY_CURRENT_STATE = PATH_MY_CURRENT_STATE;
		this.PARSER = PARSER;
	}

	@Override
	public void run() {
		try {
			UpdaterService updaterService = new UpdaterService(URL, PATH_MY_CURRENT_STATE, PARSER);
			updaterService.update();
		} catch (IOException e) {
			logger.debug(e.toString());
		}
	}

}
