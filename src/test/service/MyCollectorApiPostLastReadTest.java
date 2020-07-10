package service;

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

import model.Language;
import model.Manga;
import model.MangaState;
import utils.MangaTestUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(MyCollectorApi.class)
@TestPropertySource(properties = "mangaState.currentState=src/test/resources/MyCollectorApiPostLastReadTest.json")
public class MyCollectorApiPostLastReadTest {

	final String FILE_LOCATION = "src/test/resources/MyCollectorApiPostLastReadTest.json";

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
