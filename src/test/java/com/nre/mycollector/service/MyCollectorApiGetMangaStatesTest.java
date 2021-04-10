package com.nre.mycollector.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.utils.MangaTestsInitializer;
import com.nre.mycollector.utils.SimpleJSONFileReader;

@RunWith(SpringRunner.class)
@WebMvcTest(MyCollectorApi.class)
@TestPropertySource(properties = "path.mangastate.currentState=src/test/resources/apiGetMangaStatesCurrentStateTest.json")
public class MyCollectorApiGetMangaStatesTest {

	final String CURRENT_STATE = "src/test/resources/apiGetMangaStatesCurrentStateTest.json";

	final String PATH_EXPECTED_JSON_GET_MANGA_STATES = "src/test/resources/getMangaStatesExpected.json";

	final String PATH_EXPECTED_JSON_GET_MANGA_STATES_SORT_TO_READ = "src/test/resources/getMangaStatesSortToReadExpected.json";

	@Autowired
	private MockMvc mvc;

	@After
	public void afterEach() {
		File file = new File(CURRENT_STATE);
		file.delete();
	}

	@Test
	public void testUp() throws Exception {
		mvc.perform(get("/up").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		    .andExpect(content().string("true"));
	}

	@Test
	public void getMangaStatesTest() throws Exception {
		Map<Manga, MangaState> initState = MangaTestsInitializer.getInitCurrentState();
		StateFileService.writeCurrentState(initState, CURRENT_STATE);

		String jsonExpected = SimpleJSONFileReader.readFileAsString(PATH_EXPECTED_JSON_GET_MANGA_STATES);

		mvc.perform(get("/mangaStates").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		    .andExpect(status().isOk()).andExpect(content().json(jsonExpected));
	}

	@Test
	public void getMangaStatesSortAlphabeticTest() throws Exception {
		Map<Manga, MangaState> initState = MangaTestsInitializer.getInitCurrentStateToTestSortAlphabetic();
		StateFileService.writeCurrentState(initState, CURRENT_STATE);

		String jsonExpected = SimpleJSONFileReader.readFileAsString(PATH_EXPECTED_JSON_GET_MANGA_STATES);

		mvc.perform(get("/mangaStates").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		    .andExpect(status().isOk()).andExpect(content().json(jsonExpected));
	}

	@Test
	public void getMangaStatesSortByToReadTest() throws Exception {
		Map<Manga, MangaState> initState = MangaTestsInitializer.getInitCurrentStateToTestSortByToRead();
		StateFileService.writeCurrentState(initState, CURRENT_STATE);

		String jsonExpected = SimpleJSONFileReader.readFileAsString(PATH_EXPECTED_JSON_GET_MANGA_STATES_SORT_TO_READ);

		mvc.perform(get("/mangaStates?sort=TO_READ").contentType(MediaType.APPLICATION_JSON))
		    .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(content().json(jsonExpected));
	}

}
