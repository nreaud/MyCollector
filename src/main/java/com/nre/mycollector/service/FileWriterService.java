package com.nre.mycollector.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.springframework.stereotype.Service;

/**
 * Class to write in files - used for 600 words -> will probably disapear
 * 
 * @author nicol
 *
 */
@Service
public class FileWriterService {

	private FileWriterService() {
		//hidden
	}

	//TODO revoir trop spécifique...
	public static void writeNewContent(String fileName, Collection<String> content) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);
		fileWriter.flush();
		StringBuilder sb = new StringBuilder();
		sb.append("[");

		content.stream().sorted().forEach(line -> {
			sb.append("\"" + line + "\"," + "\r\n"); // "word,"\r\n

		});

		sb.deleteCharAt(sb.length() - 3); // remove last comma
		sb.append("]");

		fileWriter.write(sb.toString());

		fileWriter.close();

	}
}
