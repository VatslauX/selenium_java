package execution.executor;

import com.hribol.automation.core.execution.executor.TestScenario;
import com.hribol.automation.core.actions.WebDriverAction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by hvrigazov on 22.04.17.
 */
public class TestScenarioTest {

	@Test
	public void testScenarioMainsListOfActions() {
		String actionName = "actionName";
		WebDriverAction webDriverAction = mock(WebDriverAction.class);
		when(webDriverAction.getName()).thenReturn(actionName);
		when(webDriverAction.expectsHttpRequest()).thenReturn(true);

		String anotherActionName = "anotherActionName";
		WebDriverAction anotherWebDriverAction = mock(WebDriverAction.class);
		when(anotherWebDriverAction.getName()).thenReturn(anotherActionName);
		when(anotherWebDriverAction.expectsHttpRequest()).thenReturn(false);

		Optional<WebDriverAction> anotherActionOptional = Optional
				.of(anotherWebDriverAction);

		TestScenario testScenario = new TestScenario();
		testScenario.addWebDriverAction(webDriverAction);
		testScenario.addWebDriverAction(anotherActionOptional);

		assertTrue(testScenario.hasMoreSteps());
		assertTrue(testScenario.nextActionExpectsHttpRequest());
		assertEquals(actionName, testScenario.nextActionName());

		List<String> expectedActionList = new ArrayList<>();
		expectedActionList.add(actionName);
		expectedActionList.add(anotherActionName);

		assertEquals(expectedActionList, testScenario.getActions());

		WebDriverAction actionOnTop = testScenario.pollWebdriverAction();
		assertEquals(webDriverAction, actionOnTop);

		WebDriverAction lastActionInQueue = testScenario.pollWebdriverAction();
		assertEquals(anotherWebDriverAction, lastActionInQueue);

		assertFalse(testScenario.hasMoreSteps());
	}
}
