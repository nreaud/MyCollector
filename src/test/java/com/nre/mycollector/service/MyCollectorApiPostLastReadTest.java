package com.nre.mycollector.service;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@TestPropertySource(properties = "path.mangastate.currentstate=src/test/resources/apiPostLastReadCurrentStateTest.json")
public class MyCollectorApiPostLastReadTest {

	final String CURRENT_STATE = "src/test/resources/apiPostLastReadCurrentStateTest.json";

	@Autowired
	private MockMvc mvc;

	@Before
	public void initBeforeEach() throws IOException {
		Map<Manga, MangaState> initState = MangaTestUtils.getInitCurrentState();
		StateFileService.writeCurrentState(initState, CURRENT_STATE);
	}

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
	public void lastReadTest() throws Exception {

		mvc.perform(post("/mangaStates/BLACK_CLOVER/lastRead/252").contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isOk()).andExpect(jsonPath("$.manga", is(Manga.BLACK_CLOVER.name())))
		    .andExpect(jsonPath("$.lastRead", is(252))).andExpect(jsonPath("$.lastAvailable", is(252)))
		    .andExpect(jsonPath("$.lastAvailableLanguage", is(Language.FRENCH.name())));
	}

	@Test
	public void lastReadAlreadyReadTest() throws Exception {
		mvc.perform(post("/mangaStates/BLACK_CLOVER/lastRead/251").contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isOk()).andExpect(jsonPath("$.manga", is(Manga.BLACK_CLOVER.name())))
		    .andExpect(jsonPath("$.lastRead", is(251))).andExpect(jsonPath("$.lastAvailable", is(252)))
		    .andExpect(jsonPath("$.lastAvailableLanguage", is(Language.FRENCH.name())));

	}

	@Test
	public void lastReadButNotLasAvailable() throws Exception {
		mvc.perform(post("/mangaStates/BLACK_CLOVER/lastRead/250").contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isOk()).andExpect(jsonPath("$.manga", is(Manga.BLACK_CLOVER.name())))
		    .andExpect(jsonPath("$.lastRead", is(251))).andExpect(jsonPath("$.lastAvailable", is(252)))
		    .andExpect(jsonPath("$.lastAvailableLanguage", is(Language.FRENCH.name())));

	}

	@Test
	public void lastReadNotAvailableYet() throws Exception {
		mvc.perform(post("/mangaStates/BLACK_CLOVER/lastRead/253").contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isOk()).andExpect(jsonPath("$.manga", is(Manga.BLACK_CLOVER.name())))
		    .andExpect(jsonPath("$.lastRead", is(251))).andExpect(jsonPath("$.lastAvailable", is(252)))
		    .andExpect(jsonPath("$.lastAvailableLanguage", is(Language.FRENCH.name())));

	}

}
