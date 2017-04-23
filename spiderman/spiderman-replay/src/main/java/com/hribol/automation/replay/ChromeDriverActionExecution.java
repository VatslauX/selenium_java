package com.hribol.automation.replay;

import com.hribol.automation.replay.settings.ChromeDriverReplaySettings;
import com.hribol.automation.replay.settings.ReplaySettings;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by hvrigazov on 21.03.17.
 */
public class ChromeDriverActionExecution extends WebDriverActionExecutionBase {

    public ChromeDriverActionExecution(WebDriverActionExecutor webDriverActionExecutor) throws IOException, URISyntaxException {
        super(webDriverActionExecutor);
    }

    @Override
    protected ReplaySettings createExecutionSettings() {
        return new ChromeDriverReplaySettings(this.baseURI, replayRequestFilter, replayResponseFilter);
    }

    @Override
    protected String getSystemProperty() {
        return ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;
    }
}
