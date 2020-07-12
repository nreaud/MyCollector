package com.nre.mycollector.utils;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;

public class MangaStateUtils {

	private MangaStateUtils() {
		//hidden
	}

	public static void print(Map<Manga, MangaState> state, Logger logger) {
		//TODO log debug
		state.entrySet().stream().peek(entry -> logger.info(entry.toString())).collect(Collectors.toList());
	}

	public static void printMapReleases(Map<Manga, Release> state, Logger logger) {
		// TODO Auto-generated method stub
		state.entrySet().stream().peek(entry -> logger.info(entry.toString())).collect(Collectors.toList());
	}

}
