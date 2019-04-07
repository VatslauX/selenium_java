package com.github.sergueik.example;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
// import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

// based on:
// http://www.java2s.com/Tutorial/Java/0320__Network/DownloadingawebpageusingURLandURLConnectionclasses.htm
// http://www.java2s.com/Tutorials/Java/Network/HTTP_Read/Set_request_Property_for_URL_Connection_in_Java.htm
// http://www.java2s.com/Tutorial/Java/0320__Network/SendingaPOSTRequestwithParametersFromaJavaClass.htm
// http://www.java2s.com/Tutorial/Java/0320__Network/GettingtheCookiesfromanHTTPConnection.htm
// http://www.java2s.com/Tutorial/Java/0320__Network/PreventingAutomaticRedirectsinaHTTPConnection.htm

public class YellowClueTest {

	private static String mainUrl = "https://www.yellowpages.com.au/search/listings?clue=restaurant&locationClue=melbourne&lat=&lon=";
	private static String pageSource = null;
	private static int maxcount = 2;
	private final static boolean debug = true;
	private final static String defaultBrowserUserAgent = "Mozilla 5.0 (Windows; U; "
			+ "Windows NT 5.1; en-US; rv:1.8.0.11) ";
	// NOTE: the below "User-Agent" header apparently causes the test to hang
	// private static String defaultBrowserUserAgent =
	// "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) " +
	// "Gecko/20120101 Firefox/33.0";

	@Test
	public void test7() throws Exception {
		String htmlSource = null;
		for (int cnt = 0; cnt != maxcount; cnt++) {
			htmlSource = getPageHTMLSource(mainUrl);
			assertThat(htmlSource, notNullValue());
			assertTrue(htmlSource.length() > 10000);
			// assertThat(htmlSource.length(), greaterThan(10000));
			assertThat(htmlSource,
					not(containsString("we would like to ensure real humans")));

		}
	}

	// opens the url, with a specific User-Agent and returns the pagehtml
	private String getPageHTMLSource(String url) {
		return getPageHTMLSource(url, defaultBrowserUserAgent);
	}

	private String getPageHTMLSource(String url, String userAgent) {
		try {
			URLConnection urlConnection = (new URL(url)).openConnection();
			urlConnection.setRequestProperty("User-Agent", defaultBrowserUserAgent);

			InputStream inputStream = urlConnection.getInputStream();
			int byteRead;
			StringBuffer processOutput = new StringBuffer();
			while ((byteRead = inputStream.read()) != -1) {
				processOutput.append((char) byteRead);
			}
			pageSource = processOutput.toString();
		} catch (MalformedURLException e) {

		} catch (IOException e) {
		}
		if (debug) {
			System.err.println(
					String.format("url %s response: %d char", url, pageSource.length()));
			
		}
		if (debug) {
			System.out.println(
					String.format("url %s response: %d char\n%s", url, pageSource.length(), pageSource));
			
		}
		return pageSource;
	}

}
