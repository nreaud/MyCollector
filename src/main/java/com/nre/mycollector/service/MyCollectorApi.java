package com.nre.mycollector.service;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaDTO;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.SortingMangas;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class MyCollectorApi {

	@Value("${path.mangastate.currentstate}")
	private String pathMyCurrentState;

	@Autowired
	private MangaService mangaService;

	private static final Logger LOGGER = LogManager.getLogger(MyCollectorApi.class.getName());

	@GetMapping("/up")

	public boolean isUp() {
		//TODO - should be debug
		LOGGER.info("Calling isUP");
		return true;
	}

	@GetMapping("/mangas")
	public Map<Manga, MangaDTO> getMangas() {
		//TODO - should be debug
		LOGGER.info("Calling getMangas");
		return mangaService.getMangas();
	}

	@GetMapping("/mangaStates")
	public Map<Manga, MangaState> getMangaStates(
	    // defaultValue - cant use name from enum
	    @RequestParam(required = false, defaultValue = "ALPHABETIC") SortingMangas sort) throws IOException {
		//TODO - should be debug
		LOGGER.info("Calling getMangaStates with Params sort: {}", sort);
		return StateFileService.readCurrentState(pathMyCurrentState, sort);
	}

	@PostMapping("/mangaStates/{manga}/lastRead/{lastRead}")
	public MangaState postLastRead(@PathVariable("manga") Manga manga, @PathVariable("lastRead") Short lastRead)
	    throws IOException {
		//TODO - should be debug
		LOGGER.info("Calling postLastRead with params: Manga: {} and LastRead: {}", manga, lastRead);

		//TODO should not be in API class (but in other service)

		MangaState res = new MangaState();
		Map<Manga, MangaState> currentState = StateFileService.readCurrentState(pathMyCurrentState, SortingMangas.NONE);
		if (currentState.containsKey(manga)) {
			MangaState currentMangaState = currentState.get(manga);
			if (currentMangaState.getLastAvailable() >= lastRead) {
				currentMangaState.setLastRead(lastRead);
				currentMangaState.updateToRead();
				currentState.put(manga, currentMangaState);
				StateFileService.writeCurrentState(currentState, pathMyCurrentState);
				res = currentMangaState;
			} else {
				res = currentMangaState;
			}
		} else {
			//TODO 404
		}
		return res;
	}

}
