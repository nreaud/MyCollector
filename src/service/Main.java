package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import service.thread.UpdaterLauncherThread;

@SpringBootApplication
@EnableAutoConfiguration
public class Main {

	public static void main(String[] args) {
		Thread updaterLauncherThread = new UpdaterLauncherThread();
		updaterLauncherThread.start();

		/*
		 * String htmlContentWords = HttpService.getContent(
		 * "http://www.encyclopedie-incomplete.com/?Les-600-Mots-Francais-Les-Plus",
		 * false); Set<String> words = SixCentsMotsParser.parse(htmlContentWords);
		 * FileWriterService.writeNewContent("commonWords.json", words);
		 */
		SpringApplication.run(Main.class, args);

	}

}
