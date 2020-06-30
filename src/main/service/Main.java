package main.service;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import main.model.Manga;
import main.model.MangaState;
import main.model.Release;
import main.service.mapper.StateMapper;
import main.service.parser.LireScanParser;

@SpringBootApplication
public class Main {

	private static final String PATH_MY_CURRENT_STATE = "src/main/resource/mangaState.json";

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		try {

			String htmlContent = HttpService.getContent("https://www.lirescan.me/",
					true);

			Map<Manga, Release> mapLatestReleases = LireScanParser.parse(htmlContent);

			Map<Manga, MangaState> lireScanNewStates = StateMapper
					.mapFromReleases(mapLatestReleases);

			System.out.println("=== LireScan current state ===");
			lireScanNewStates.entrySet().stream()
					.peek(entry -> System.out.println(entry))
					.collect(Collectors.toList());

			Map<Manga, MangaState> myCurrentStates = StateWriterService
					.readCurrentStateSorted(PATH_MY_CURRENT_STATE);

			System.out.println("=== My current state ===");
			myCurrentStates.entrySet().stream()
					.peek(entry -> System.out.println(entry))
					.collect(Collectors.toList());

			Map<Manga, MangaState> updates = Controller.getUpdates(myCurrentStates,
					lireScanNewStates);
			if (!updates.isEmpty()) {
				myCurrentStates = Controller.update(myCurrentStates, updates);

				System.out.println("=== My current state after update ===");
				myCurrentStates.entrySet().stream()
						.peek(entry -> System.out.println(entry))
						.collect(Collectors.toList());

				StateWriterService.writeCurrentState(myCurrentStates, PATH_MY_CURRENT_STATE);
			}

			/*
			 * String htmlContentWords = HttpService.getContent(
			 * "http://www.encyclopedie-incomplete.com/?Les-600-Mots-Francais-Les-Plus",
			 * false); Set<String> words = SixCentsMotsParser.parse(htmlContentWords);
			 * FileWriterService.writeNewContent("commonWords.json", words);
			 */

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
