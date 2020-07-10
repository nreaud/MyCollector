package service;

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

import model.Language;
import model.Manga;
import model.MangaState;
import model.Release;
import service.parser.MangaWebSiteParser;
import utils.MangaTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "service.*")
public class UpdaterServiceTest {

	final String FILE_LOCATION = "src/test/resources/updaterServiceTest.json";
	final String DUMMY_URL = "any";
	String dummyHtmlContent = "dummyHtmlContent";

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
	public void updateTestOk() throws IOException {
		//=== GIVEN ===
		//TODO mock only http call
		PowerMockito.mockStatic(HttpService.class); //power mockito to mock static methods
		PowerMockito.when(HttpService.getContent(DUMMY_URL, true)).thenReturn(dummyHtmlContent);

		//TODO call really parser and test parser later when parser intelligent
		MangaWebSiteParser mockParser = PowerMockito.mock(MangaWebSiteParser.class);
		PowerMockito.when(mockParser.parse(dummyHtmlContent)).thenReturn(getMapReleases());

		//=== WHEN ===
		UpdaterService updaterService = new UpdaterService(DUMMY_URL, FILE_LOCATION, mockParser);
		updaterService.update();

		//=== EXPECT ===
		Map<Manga, MangaState> stateAfterUpdate = StateWriterService.readCurrentState(FILE_LOCATION);
		assertEquals(2, stateAfterUpdate.size());
		MangaState ajin = stateAfterUpdate.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajin.getManga());
		assertEquals(new Short((short) 77), ajin.getLastAvailable());
		assertEquals(Language.FRENCH, ajin.getLastAvailableLanguage());

		MangaState bl = stateAfterUpdate.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, bl.getManga());
		assertEquals(new Short((short) 253), bl.getLastAvailable());
		assertEquals(Language.SPOIL, bl.getLastAvailableLanguage());
	}

	@Test
	public void shouldNotUpdate() throws IOException {
		//TODO mock only http call
		PowerMockito.mockStatic(HttpService.class); //power mockito to mock static methods
		PowerMockito.when(HttpService.getContent(DUMMY_URL, true)).thenReturn(dummyHtmlContent);

		//TODO call really parser and test parser later when parser intelligent
		MangaWebSiteParser mockParser = PowerMockito.mock(MangaWebSiteParser.class);
		PowerMockito.when(mockParser.parse(dummyHtmlContent)).thenReturn(getMapReleasesNoUpdates());

		//=== WHEN ===
		UpdaterService updaterService = new UpdaterService(DUMMY_URL, FILE_LOCATION, mockParser);
		updaterService.update();

		//=== EXPECT ===
		Map<Manga, MangaState> stateAfterUpdate = StateWriterService.readCurrentState(FILE_LOCATION);
		assertEquals(2, stateAfterUpdate.size());
		MangaState ajin = stateAfterUpdate.get(Manga.AJIN);
		assertEquals(Manga.AJIN, ajin.getManga());
		assertEquals(new Short((short) 77), ajin.getLastAvailable());
		assertEquals(Language.ENGLISH, ajin.getLastAvailableLanguage());

		MangaState bl = stateAfterUpdate.get(Manga.BLACK_CLOVER);
		assertEquals(Manga.BLACK_CLOVER, bl.getManga());
		assertEquals(new Short((short) 252), bl.getLastAvailable());
		assertEquals(Language.FRENCH, bl.getLastAvailableLanguage());

	}

	private Map<Manga, Release> getMapReleases() {
		Map<Manga, Release> res = new HashMap<>();
		res.put(Manga.AJIN, new Release(Manga.AJIN, 77, Language.FRENCH));
		res.put(Manga.BLACK_CLOVER, new Release(Manga.BLACK_CLOVER, 253, Language.SPOIL));
		return res;
	}

	private Map<Manga, Release> getMapReleasesNoUpdates() {
		Map<Manga, Release> res = new HashMap<>();
		res.put(Manga.AJIN, new Release(Manga.AJIN, 77, Language.RAW));
		res.put(Manga.BLACK_CLOVER, new Release(Manga.BLACK_CLOVER, 251, Language.SPOIL));
		return res;
	}

}
