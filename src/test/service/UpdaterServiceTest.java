package service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "service.*")
public class UpdaterServiceTest {

	@Test
	public void updateTestOk() throws IOException {
		//=== GIVEN ===
		Map<Manga, MangaState> initState = getInitState();
		final String FILE_LOCATION = "src/test/resource/updateTestOk.json";
		StateWriterService.writeCurrentState(initState, FILE_LOCATION);

		final String DUMMY_URL = "any";
		//TODO mock only http call
		PowerMockito.mockStatic(HttpService.class); //power mockito to mock static methods
		String dummyHtmlContent = "dummyHtmlContent";
		PowerMockito.when(HttpService.getContent(DUMMY_URL, true)).thenReturn(dummyHtmlContent);

		//TODO call really and test parser later when parser intelligent
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

	private Map<Manga, MangaState> getInitState() {
		Map<Manga, MangaState> res = new HashMap<>();
		//AJIN - test update language
		res.put(Manga.AJIN, new MangaState(Manga.AJIN, (short) 77, Language.ENGLISH));
		//BLACK CLOVER - test update number
		res.put(Manga.BLACK_CLOVER, new MangaState(Manga.BLACK_CLOVER, (short) 252, Language.FRENCH));
		return res;
	}

	private Map<Manga, Release> getMapReleases() {
		Map<Manga, Release> res = new HashMap<>();
		res.put(Manga.AJIN, new Release(Manga.AJIN, 77, Language.FRENCH));
		res.put(Manga.BLACK_CLOVER, new Release(Manga.BLACK_CLOVER, 253, Language.SPOIL));
		return res;
	}

}
