package com.nre.mycollector.service;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.nre.mycollector.model.Manga;

@RunWith(SpringRunner.class)
@WebMvcTest(MyCollectorApi.class)
public class MyCollectorApiGetMangasTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void testUp() throws Exception {
		mvc.perform(get("/up").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		    .andExpect(content().string("true"));
	}

	@Test
	public void testGetMangas() throws Exception {
		Manga[] mangasExpected = Manga.values();
		mvc.perform(get("/mangas").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		    .andExpect(jsonPath("$", hasSize(mangasExpected.length)));

	}

}
