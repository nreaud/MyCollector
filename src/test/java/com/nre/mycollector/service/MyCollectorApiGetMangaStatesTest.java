package com.nre.mycollector.service;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.nre.mycollector.model.Language;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.utils.MangaTestUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(MyCollectorApi.class)
@TestPropertySource(properties = "currentState=src/test/resources/MyCollectorApiGetMangaStatesTest.json")
public class MyCollectorApiGetMangaStatesTest {

	final String FILE_LOCATION = "src/test/resources/MyCollectorApiGetMangaStatesTest.json";

	@Autowired
	private MockMvc mvc;

	@Before
	public void initBeforeEach() throws IOException {
		Map<Manga, MangaState> initState = MangaTestUtils.getInitState();
		StateWriterService.writeCurrentState(initState, FILE_LOCATION);
	}

	@After
	public void afterEach() {
		File file = new File(FILE_LOCATION);
		file.delete();
	}

	@Test
	public void testUp() throws Exception {
		mvc.perform(get("/up").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		    .andExpect(content().string("true"));
	}

	@Test
	public void getMangaStatesTest() throws Exception {
		mvc.perform(get("/mangaStates").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		    .andExpect(jsonPath("$.AJIN.manga", is(Manga.AJIN.name())))
		    .andExpect(jsonPath("$.AJIN.lastRead", is((Short) null))).andExpect(jsonPath("$.AJIN.lastAvailable", is(77)))
		    .andExpect(jsonPath("$.AJIN.lastAvailableLanguage", is(Language.ENGLISH.name())))
		    .andExpect(jsonPath("$.BLACK_CLOVER.manga", is(Manga.BLACK_CLOVER.name())))
		    .andExpect(jsonPath("$.BLACK_CLOVER.lastRead", is(251)))
		    .andExpect(jsonPath("$.BLACK_CLOVER.lastAvailable", is(252)))
		    .andExpect(jsonPath("$.BLACK_CLOVER.lastAvailableLanguage", is(Language.FRENCH.name())));
	}

}
