package com.nre.mycollector.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleJSONFileReader {

	public static String readFileAsString(String file) throws Exception {
		return new String(Files.readAllBytes(Paths.get(file)));
	}

}
