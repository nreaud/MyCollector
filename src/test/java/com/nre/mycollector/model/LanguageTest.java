package com.nre.mycollector.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.nre.mycollector.model.Language;

public class LanguageTest {

	@Test
	public void TestMoreRecent() {
		assertTrue(Language.moreRecent(Language.FRENCH, Language.ENGLISH));
		assertFalse(Language.moreRecent(Language.SPOIL, Language.RAW));
	}
}
