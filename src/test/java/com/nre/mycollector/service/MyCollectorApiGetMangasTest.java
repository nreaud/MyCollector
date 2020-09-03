package com.nre.mycollector.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.nre.mycollector.utils.SimpleJSONFileReader;

@RunWith(SpringRunner.class)
@WebMvcTest(MyCollectorApi.class)
@TestPropertySource(properties = { "path.mangasimg.ajin=src/test/resources/mangasImg/Ajin15.jpg",
    "path.mangasimg.blackclover=src/test/resources/mangasImg/BlackClover8.jpg" })
public class MyCollectorApiGetMangasTest {

	@Autowired
	private MockMvc mvc;

	final String PATH_EXPECTED_JSON_GET_MANGAS = "src/test/resources/getMangasExpected.json";

	@Test
	public void testUp() throws Exception {
		mvc.perform(get("/up").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		    .andExpect(content().string("true"));
	}

	@Test
	public void testGetMangas() throws Exception {
		String jsonExpected = SimpleJSONFileReader.readFileAsString(PATH_EXPECTED_JSON_GET_MANGAS);

		mvc.perform(get("/mangas").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		    .andExpect(content().json(jsonExpected));

	}

}
