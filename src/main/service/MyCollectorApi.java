package service;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import model.Manga;
import model.MangaState;

@RestController
public class MyCollectorApi {

	private static final String PATH_MY_CURRENT_STATE = "src/main/resources/mangaState.json";

	@GetMapping("/up")
	public boolean isUp() {
		return true;
	}

	@GetMapping("/mangaStates")
	public Map<Manga, MangaState> getMangaStates() throws JsonParseException, JsonMappingException, IOException {

		return StateWriterService.readCurrentStateSorted(PATH_MY_CURRENT_STATE);
	}

	@PostMapping("/mangaStates/{manga}/lastRead/{lastRead}")
	public MangaState postLastRead(@PathVariable("manga") Manga manga, @PathVariable("lastRead") Short lastRead)
	    throws JsonParseException, JsonMappingException, IOException {

		MangaState res = new MangaState();
		TreeMap<Manga, MangaState> currentState = StateWriterService.readCurrentStateSorted(PATH_MY_CURRENT_STATE);
		if (currentState.containsKey(manga)) {
			MangaState currentMangaState = currentState.get(manga);
			if (currentMangaState.getLastRead() < lastRead) {
				currentMangaState.setLastRead(lastRead);
				currentState.put(manga, currentMangaState);
				StateWriterService.writeCurrentState(currentState, PATH_MY_CURRENT_STATE);
				res = currentMangaState;
			}
		} else {
			// TODO 404
		}
		return res;
	}
}
