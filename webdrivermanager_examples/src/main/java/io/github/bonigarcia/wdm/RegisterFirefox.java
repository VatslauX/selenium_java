/*
 * (C) Copyright 2017 Boni Garcia (http://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.wdm;

import org.openqa.grid.selenium.GridLauncherV3;

public class RegisterFirefox {

	public static void main(String[] args) throws Exception {
		FirefoxDriverManager.getInstance().setup();
		GridLauncherV3.main(new String[] { "-role", "node", "-hub",
				"http://localhost:4444/grid/register", "-browser",
				"browserName=firefox,version=54", "-port", "5556" });
	}

}
