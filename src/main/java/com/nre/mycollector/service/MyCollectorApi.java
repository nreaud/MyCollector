package com.nre.mycollector.service;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;

@RestController
public class MyCollectorApi {

	//TODO - try spring way (config class)
	@Value("${currentstate}")
	private String pathMyCurrentState;

	private static final Logger LOGGER = LogManager.getLogger(MyCollectorApi.class.getName());

	@GetMapping("/up")

	public boolean isUp() {
		//TODO - should be debug
		LOGGER.info("Calling isUP");
		return true;
	}

	@GetMapping("/mangaStates")
	public Map<Manga, MangaState> getMangaStates() throws IOException {
		//TODO - should be debug
		LOGGER.info("Calling getMangaStates");
		return StateWriterService.readCurrentStateSorted(pathMyCurrentState);
	}

	@PostMapping("/mangaStates/{manga}/lastRead/{lastRead}")
	public MangaState postLastRead(@PathVariable("manga") Manga manga, @PathVariable("lastRead") Short lastRead)
	    throws IOException {
		//TODO - should be debug
		LOGGER.info("Calling postLastRead with params: Manga: {} and LastRead: {}", manga, lastRead);

		MangaState res = new MangaState();
		SortedMap<Manga, MangaState> currentState = StateWriterService.readCurrentStateSorted(pathMyCurrentState);
		if (currentState.containsKey(manga)) {
			MangaState currentMangaState = currentState.get(manga);
			if (currentMangaState.getLastRead() < lastRead && currentMangaState.getLastAvailable() >= lastRead) {
				currentMangaState.setLastRead(lastRead);
				currentState.put(manga, currentMangaState);
				StateWriterService.writeCurrentState(currentState, pathMyCurrentState);
				res = currentMangaState;
			} else {
				res = currentMangaState;
			}
		} else {
			// TODO 404
		}
		return res;
	}
}
