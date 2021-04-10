package com.nre.mycollector.service.parser;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nre.mycollector.service.HttpService;

@RunWith(SpringRunner.class)
@WebMvcTest(SmartMangaWebSiteParser.class)
@TestPropertySource(properties = "path.mangastate.currentState=src/test/resources/tmpCurrentStateTest.json, "
    + "path.mangastate.lelscanstate= src/test/resources/tmpLelscanStateTest.json")
public class SmartMangaWebSiteParserTest {

	@Autowired
	private HttpService httpService;

	@Test
	public void developpingTest() throws IOException {
		String htmlContent = httpService.getContent("https://www.lelscan-vf.net/");
		SmartMangaWebSiteParser parser = new SmartMangaWebSiteParser("src/test/resources/tmpLelscanStateTest.json");
		parser.parse(htmlContent);
	}
}
