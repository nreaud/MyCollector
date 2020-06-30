package main.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Class to write in files - used for 600 words -> will probably disapear
 * 
 * @author nicol
 *
 */
public class FileWriterService {
	// TODO revoir trop spécifique...
	public static void writeNewContent(String fileName,
			Collection<String> content) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);
		fileWriter.flush();
		StringBuilder sb = new StringBuilder();
		sb.append("[");

		content.stream().sorted().peek(line -> {
			sb.append("\"" + line + "\"," + "\r\n"); // "word,"\r\n

		}).collect(Collectors.toList());

		sb.deleteCharAt(sb.length() - 3); // remove last comma
		sb.append("]");

		fileWriter.write(sb.toString());

		fileWriter.close();

	}
}
