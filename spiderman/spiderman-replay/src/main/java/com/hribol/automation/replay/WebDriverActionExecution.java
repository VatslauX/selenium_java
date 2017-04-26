package com.hribol.automation.replay;

import com.hribol.automation.core.execution.executor.AutomationResult;
import com.hribol.automation.core.execution.executor.TestScenario;
import com.hribol.automation.core.suite.VirtualScreenProcessCreator;
import com.hribol.automation.core.utils.LoadingTimes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * Created by hvrigazov on 15.03.17.
 */
public interface WebDriverActionExecution {
	void execute(TestScenario testScenario)
			throws InterruptedException, IOException, URISyntaxException;

	void executeOnScreen(TestScenario testScenario, String screenToUse)
			throws InterruptedException, IOException, URISyntaxException;

	void dumpHarMetrics(String fileNameToDump) throws IOException;

	void dumpLoadingTimes(String fileNameToDump)
			throws UnsupportedEncodingException, FileNotFoundException;

	LoadingTimes getLoadingTimes();

	AutomationResult getAutomationResult();

	AutomationResult executeOnScreen(TestScenario testScenario, int i,
			VirtualScreenProcessCreator virtualScreenProcessCreator,
			String loadingTimesFileName, String harTimesFileName);
}
