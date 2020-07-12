package com.nre.mycollector.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;

/**
 * This class load and unload MangaState from a json file
 * 
 * @author nicol
 *
 */
@Service
public class StateFileService {

	//TODO not duplicated methods with MangaState & Release (maybe T?)

	private StateFileService() {
		//hidden
	}

	/**
	 * Read all the json file and extract the corresponding mangaState if present
	 * 
	 * @param pathMyCurrentState
	 * @param manga
	 * @return the mangaState if present, optional of null otherwise
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Optional<MangaState> readCurrentMangaState(String pathMyCurrentState, Manga manga) throws IOException {
		Map<Manga, MangaState> currentState = readCurrentState(pathMyCurrentState);
		Optional<MangaState> res;
		if (currentState.containsKey(manga)) {
			res = Optional.of(currentState.get(manga));
		} else {
			res = Optional.ofNullable(null);
		}
		return res;
	}

	public static Map<Manga, MangaState> readCurrentState(String fileLocation) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<Map<Manga, MangaState>> typeRef = new TypeReference<Map<Manga, MangaState>>() {
		}; // cause can't have .class from generic
		return objectMapper.readValue(new File(fileLocation), typeRef);
	}

	public static SortedMap<Manga, MangaState> readCurrentStateSorted(String fileLocation) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<TreeMap<Manga, MangaState>> typeRef = new TypeReference<TreeMap<Manga, MangaState>>() {
		}; // cause can't have .class from generic
		return objectMapper.readValue(new File(fileLocation), typeRef);
	}

	public static Map<Manga, Release> readWebSiteState(String pathCurrentStateWebSite) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<Map<Manga, Release>> typeRef = new TypeReference<Map<Manga, Release>>() {
		}; // cause can't have .class from generic
		return objectMapper.readValue(new File(pathCurrentStateWebSite), typeRef);
	}

	public static SortedMap<Manga, Release> readWebSiteStateSorted(String pathCurrentStateWebSite) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<TreeMap<Manga, Release>> typeRef = new TypeReference<TreeMap<Manga, Release>>() {
		}; // cause can't have .class from generic
		return objectMapper.readValue(new File(pathCurrentStateWebSite), typeRef);
	}

	public static void writeCurrentState(Map<Manga, MangaState> state, String fileLocation) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		// create an instance of DefaultPrettyPrinter
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

		Path pathToFile = Paths.get(fileLocation);
		Files.createDirectories(pathToFile.getParent());
		File file = new File(fileLocation);
		file.createNewFile();
		// convert map to JSON file
		writer.writeValue(file, state);
	}

	public static void writeWebSiteState(Map<Manga, Release> currentStateWebSite, String pathCurrentStateWebSite)
	    throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		// create an instance of DefaultPrettyPrinter
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

		Path pathToFile = Paths.get(pathCurrentStateWebSite);
		Files.createDirectories(pathToFile.getParent());
		File file = new File(pathCurrentStateWebSite);
		file.createNewFile();
		// convert map to JSON file
		writer.writeValue(file, currentStateWebSite);

	}

}