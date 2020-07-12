package com.nre.mycollector.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.nre.mycollector.model.Language;
import com.nre.mycollector.model.Manga;
import com.nre.mycollector.model.MangaState;
import com.nre.mycollector.model.Release;
import com.nre.mycollector.service.parser.MangaWebSiteParser;
import com.nre.mycollector.utils.MangaTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "com.nre.mycollector.service.*")
public class UpdaterServiceTest {

	private static final double DELTA = 0.0;
	final String CURRENT_STATE = "src/test/resources/updaterServiceCurrentStateTest.json";
	final String LIRESCAN_STATE = "src/test/resources/updaterServiceLireScanStateTest.json";
	final String DUMMY_URL = "any";
	String dummyHtmlContent = "dummyHtmlContent";

	@Before
	public void initBeforeEach() throws IOException {
		Map<Manga, MangaState> currentState = MangaTestUtils.getInitCurrentState();
		StateFileService.writeCurrentState(currentState, CURRENT_STATE);

		Map<Manga, Release> lireScanState = MangaTestUtils.getInitLireScanState();
		StateFileService.writeWebSiteState(lireScanState, LIRESCAN_STATE);
	}

	@After
	public void afterEach() {
		File currentStateFile = new File(CURRENT_STATE);
		File lireScanFile = new File(LIRESCAN_STATE);
		lireScanFile.delete();
		currentStateFile.delete();
	}

	@Test
	public void updateTestOk() throws IOException {
		//=== GIVEN ===
		//TODO mock only http call
		PowerMockito.mockStatic(HttpService.class); //power mockito to mock static methods
		PowerMockito.when(HttpService.getContent(DUMMY_URL)).thenReturn(dummyHtmlContent);

		//TODO call really parser and test parser later when parser intelligent
		MangaWebSiteParser mockParser = PowerMockito.mock(MangaWebSiteParser.class);
		PowerMockito.when(mockParser.parse(dummyHtmlContent)).thenReturn(getMapReleases());

		//=== WHEN ===
		UpdaterService updaterService = new UpdaterService(DUMMY_URL, LIRESCAN_STATE, CURRENT_STATE, mockParser);
		updaterService.update();

		//=== CURRENT STATE JSON SHOULD BE UPDATED ===
		Map<Manga, MangaState> stateAfterUpdate = StateFileService.readCurrentState(CURRENT_STATE);
		assertEquals(2, stateAfterUpdate.size());
		MangaState ajin = stateAfterUpdate.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajin.getManga());
		assertEquals(new Short((short) 77), ajin.getLastAvailable());
		assertEquals(Language.FRENCH, ajin.getLastAvailableLanguage());

		MangaState bl = stateAfterUpdate.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, bl.getManga());
		assertEquals(new Short((short) 253), bl.getLastAvailable());
		assertEquals(Language.SPOIL, bl.getLastAvailableLanguage());

		//=== LIRESCAN JSON SHOULD BE UPDATED ===
		Map<Manga, Release> lirescanState = StateFileService.readWebSiteState(LIRESCAN_STATE);
		assertEquals(2, lirescanState.size());
		Release ajinLs = lirescanState.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajinLs.getManga());
		assertEquals(77, ajinLs.getNumber(), DELTA);
		assertEquals(Language.FRENCH, ajinLs.getLanguage());

		Release blLs = lirescanState.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, blLs.getManga());
		assertEquals(253, blLs.getNumber(), DELTA);
		assertEquals(Language.SPOIL, blLs.getLanguage());
	}

	@Test
	public void shouldNotUpdate() throws IOException {
		//TODO mock only http call
		PowerMockito.mockStatic(HttpService.class); //power mockito to mock static methods
		PowerMockito.when(HttpService.getContent(DUMMY_URL)).thenReturn(dummyHtmlContent);

		//TODO call really parser and test parser later when parser intelligent
		MangaWebSiteParser mockParser = PowerMockito.mock(MangaWebSiteParser.class);
		PowerMockito.when(mockParser.parse(dummyHtmlContent)).thenReturn(getMapReleasesNoUpdates());

		//=== WHEN ===
		UpdaterService updaterService = new UpdaterService(DUMMY_URL, LIRESCAN_STATE, CURRENT_STATE, mockParser);
		updaterService.update();

		//=== EXPECT ===
		Map<Manga, MangaState> stateAfterUpdate = StateFileService.readCurrentState(CURRENT_STATE);
		assertEquals(2, stateAfterUpdate.size());
		MangaState ajin = stateAfterUpdate.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajin.getManga());
		assertEquals(new Short((short) 77), ajin.getLastAvailable());
		assertEquals(Language.ENGLISH, ajin.getLastAvailableLanguage());

		MangaState bl = stateAfterUpdate.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, bl.getManga());
		assertEquals(new Short((short) 252), bl.getLastAvailable());
		assertEquals(Language.FRENCH, bl.getLastAvailableLanguage());

		//=== LIRESCAN JSON SHOULD NOT BE UPDATED EITHER ===
		Map<Manga, Release> lirescanState = StateFileService.readWebSiteState(LIRESCAN_STATE);
		assertEquals(2, lirescanState.size());
		Release ajinLs = lirescanState.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajinLs.getManga());
		assertEquals(77, ajinLs.getNumber(), DELTA);
		assertEquals(Language.RAW, ajinLs.getLanguage());

		Release blLs = lirescanState.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, blLs.getManga());
		assertEquals(251, blLs.getNumber(), DELTA);
		assertEquals(Language.FRENCH, blLs.getLanguage());

	}

	@Test
	public void shouldOnlyUpdateLireScan() throws IOException {
		//TODO mock only http call
		PowerMockito.mockStatic(HttpService.class); //power mockito to mock static methods
		PowerMockito.when(HttpService.getContent(DUMMY_URL)).thenReturn(dummyHtmlContent);

		//TODO call really parser and test parser later when parser intelligent
		MangaWebSiteParser mockParser = PowerMockito.mock(MangaWebSiteParser.class);
		PowerMockito.when(mockParser.parse(dummyHtmlContent)).thenReturn(getMapReleasesUpdateLireScanOnly());

		//=== WHEN ===
		UpdaterService updaterService = new UpdaterService(DUMMY_URL, LIRESCAN_STATE, CURRENT_STATE, mockParser);
		updaterService.update();

		//=== EXPECT ===
		Map<Manga, MangaState> stateAfterUpdate = StateFileService.readCurrentState(CURRENT_STATE);
		assertEquals(2, stateAfterUpdate.size());
		MangaState ajin = stateAfterUpdate.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajin.getManga());
		assertEquals(new Short((short) 77), ajin.getLastAvailable());
		assertEquals(Language.ENGLISH, ajin.getLastAvailableLanguage());

		MangaState bl = stateAfterUpdate.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, bl.getManga());
		assertEquals(new Short((short) 252), bl.getLastAvailable());
		assertEquals(Language.FRENCH, bl.getLastAvailableLanguage());

		//=== LIRESCAN JSON SHOULD BE UPDATED ===
		Map<Manga, Release> lirescanState = StateFileService.readWebSiteState(LIRESCAN_STATE);
		assertEquals(2, lirescanState.size());
		Release ajinLs = lirescanState.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajinLs.getManga());
		assertEquals(77, ajinLs.getNumber(), DELTA);
		assertEquals(Language.ENGLISH, ajinLs.getLanguage());

		Release blLs = lirescanState.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, blLs.getManga());
		assertEquals(252, blLs.getNumber(), DELTA);
		assertEquals(Language.SPOIL, blLs.getLanguage());
	}

	private Map<Manga, Release> getMapReleasesUpdateLireScanOnly() {
		Map<Manga, Release> res = new HashMap<>();
		res.put(Manga.AJIN, new Release(Manga.AJIN, 77, Language.ENGLISH));
		res.put(Manga.BLACK_CLOVER, new Release(Manga.BLACK_CLOVER, 252, Language.SPOIL));
		return res;
	}

	private Map<Manga, Release> getMapReleases() {
		Map<Manga, Release> res = new HashMap<>();
		res.put(Manga.AJIN, new Release(Manga.AJIN, 77, Language.FRENCH));
		res.put(Manga.BLACK_CLOVER, new Release(Manga.BLACK_CLOVER, 253, Language.SPOIL));
		return res;
	}

	private Map<Manga, Release> getMapReleasesNoUpdates() {
		Map<Manga, Release> res = new HashMap<>();
		res.put(Manga.AJIN, new Release(Manga.AJIN, 77, Language.SPOIL));
		res.put(Manga.BLACK_CLOVER, new Release(Manga.BLACK_CLOVER, 250, Language.SPOIL));
		return res;
	}

}
