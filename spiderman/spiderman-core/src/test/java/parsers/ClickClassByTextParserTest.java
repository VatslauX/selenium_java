package parsers;

import com.hribol.automation.core.actions.WebDriverAction;
import com.hribol.automation.core.parsers.ClickClassByTextParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by hvrigazov on 21.04.17.
 */
public class ClickClassByTextParserTest {

	@Test
	public void canParseCorrectMap() {
		Map<String, Object> map = new HashMap<>();

		String initialCollectorClass = "mega-menu-click";
		String text = "ATP";
		String eventName = "clickMegaMenu";
		boolean expectsHttp = true;

		map.put("initialCollectorClass", initialCollectorClass);
		map.put("text", text);
		map.put("event", eventName);
		map.put("expectsHttp", expectsHttp);
		ClickClassByTextParser clickClassByTextParser = new ClickClassByTextParser();
		WebDriverAction webDriverAction = clickClassByTextParser.create(map);

		assertEquals(eventName, webDriverAction.getName());
		assertEquals(expectsHttp, webDriverAction.expectsHttpRequest());
	}
}
