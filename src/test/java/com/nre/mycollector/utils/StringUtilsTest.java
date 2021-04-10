package com.nre.mycollector.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void textContainedInAtLeastOneTestShouldBeOk() {
		assertTrue(StringUtils.textContainedInAtLeastOne("toto", "aaa toto aaaa", "foobar", "unrelated"));
	}

	@Test
	public void textContainedInAtLeastOneCaseInsensitiveTestShouldBeOk() {
		assertTrue(StringUtils.textContainedInAtLeastOne("toto", false, "aaa TOTO aaaa", "foobar", "unrelated"));
	}

	@Test
	public void textContainedInAtLeastOneCaseSensitiveTestShouldNotBeFound() {
		assertFalse(StringUtils.textContainedInAtLeastOne("toto", true, "aaa TOTO aaaa", "foobar", "unrelated"));
	}

	@Test
	public void textContainedInAtLeastOneTestShouldNotBeFound() {
		assertFalse(StringUtils.textContainedInAtLeastOne("toto", "foobar", "unrelated"));
	}

	@Test
	public void textContainedInAtLeastOneTestShouldNotBug() {
		assertFalse(StringUtils.textContainedInAtLeastOne("toto"));
	}

}
