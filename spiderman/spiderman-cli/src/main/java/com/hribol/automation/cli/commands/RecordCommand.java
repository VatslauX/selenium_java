package com.hribol.automation.cli.commands;

import com.hribol.automation.record.ChromeRecordBrowser;
import com.hribol.automation.record.RecordBrowserBase;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by hvrigazov on 11.04.17.
 */
public class RecordCommand implements Command {
    private final String pathToChromedriver;
    private final String pathToJSInjectionFile;
    private final String baseUrl;
    private final String ouputFile;

    public RecordCommand(String pathToChromedriver,
                         String pathToJSInjectionFile,
                         String baseUrl,
                         String ouputFile) {
        this.pathToChromedriver = pathToChromedriver;
        this.pathToJSInjectionFile = pathToJSInjectionFile;
        this.baseUrl = baseUrl;
        this.ouputFile = ouputFile;
    }

    @Override
    public void run() {
        RecordBrowserBase recordBrowserBase = new ChromeRecordBrowser(pathToChromedriver, pathToJSInjectionFile);
        try {
            recordBrowserBase.record(baseUrl);
            System.out.println("Press Enter when finished recording");
            System.in.read();
            recordBrowserBase.dumpActions(this.ouputFile);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        recordBrowserBase.cleanUp();
    }
}
