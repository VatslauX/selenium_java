
### Info

![icon](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/document_wrench_color.png)

__Selenium WebDriver Elementor Toolkit__ (__SWET__) is a OS-independent successor to the [Selenium WebDriver Page Recorder](https://github.com/dzharii/swd-recorder) of
Dmytro Zharii (and author) - a.k.a. __SWD__. __SWET__ is using the
[Eclipse Standard Widget Toolkit](https://www.eclipse.org/swt/) with third party widgets
(currently, [Opal](https://github.com/lcaron/opal) instead of .Net Windows Forms for user interface and
[Jtwig](http://jtwig.org/documentation/reference/tags/control-flow)
 template engine instead of [ASP.Net Razor](https://en.wikipedia.org/wiki/ASP.NET_Razor) for code generation (that is just one of the available template exngines - note, __jtwig__ supports the original [PHP Twig](http://twig.sensiolabs.org/doc/2.x/) syntax)..
Therefore __SWET__ runs on Windows, Mac or Linux, 32 or 64 bit platforms.
Eventually the full functionality of __SWD__ is to be achieved, also __SWET__ might become an [Eclipse plugin](http://www.vogella.com/tutorials/EclipsePlugin/article.html).

The application is developed in Ecipse with [SWT Designer/Window Builder](http://www.vogella.com/tutorials/EclipseWindowBuilder/article.html),
on Ubuntu 16.04 and Windows.
For Mac / Safari testing, the [Sierra Final 10.12](https://techsviewer.com/install-macos-sierra-virtualbox-windows/) Virtual Box by TechReviews is being used.
Currently, working with Safari browser is somewhat flaky.


![OSX Example](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/capture1.png)

![Ubuntu Example](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/capture2.png)

![Windows Example](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/capture3.png)

### Usage

__SWET__ is currently beta quality, it may not be very useful: one can create a session Page Element recording in Java or C#,
In order to use one will have to compile the application - it is not difficult.
Continue reading for info on how to get the dev environment setup.

### Prerequisites
Project needs JDK 1.8 or later and Maven to be installed and in the `PATH`. This means the following environment variables are to
either be defined globally:

```bash
JAVA_VERSION
JAVA_HOME
M2_HOME
MAVEN_VERSION
M2
```
or updated in the project runner scripts as explained below.

The Eclipse is not required. With the exception of one jar, the project dependencies are pulled by Maven. On a Mac, the
JDK is expected to be installed to
`/Library/Java/JavaVirtualMachines/jdk$JAVA_VERSION.jdk/Contents/Home` which is the default location.
On Windows Java and Maven were conveniently installed to `c:\java\`.
#### Updating the platform-specific information in the `pom.xml`

The project `pom.xml` currently is declaring the main `swt.jar` dependency in a platform-specific fashion:

```xml
  <properties>
    <eclipse.swt.version>4.3</eclipse.swt.version>
    <eclipse.swt.artifactId>org.eclipse.swt.win32.win32.x86_64</eclipse.swt.artifactId>
    <!--
    <eclipse.swt.artifactId>org.eclipse.swt.gtk.linux.x86_64</eclipse.swt.artifactId>
    <eclipse.swt.artifactId>org.eclipse.swt.gtk.linux.x86</eclipse.swt.artifactId>
    <eclipse.swt.artifactId>org.eclipse.swt.cocoa.macosx</eclipse.swt.artifactId>
    <eclipse.swt.artifactId>org.eclipse.swt.cocoa.macosx.x86_64</eclipse.swt.artifactId>
    -->
  </properties>
  <dependencies>
    <dependency>
			<groupId>org.eclipse.swt</groupId>
			<artifactId>${eclipse.swt.artifactId}</artifactId>
      <version>${eclipse.swt.version}</version>
		</dependency>
    ...
```
One willl have to uncomment the relevant `artifactId` property definition and comment the rest.
Due to some problem with JVM loader, these platform-dependent jars cannot be included simultaneously.
The alternative is to package the spring-boot jar file as explained in
[Multiplatform SWT](https://github.com/jendap/multiplatform-swt) project.
Unfortulately the resulting bare-bones `multiplatform-swt-loader.jar` file is almost 10 Mb and __SWET__ jar is over 30 Mb.
Therefore,  we recommend to modify the `pom.xml` and use runner scripts explained below.

#### Runner Scripts
After the project is cloned or downloaded from from github, one will find the following `run.*` scripts helpful to compile and start the application:
On Windows, use either Powershell script
```powershell
. .\run.ps1
```
or a batch file
```cmd
run.cmd
```
On Umix /Mac, run Bash script
```bash
./run.sh
```

The script downloads those dependency jar(s), that are not hosted on Maven Central repository,
compiles and packages the project using maven
and runs the application jar from the `target` directory.

The script configuration needs to be updated with the actual paths to Java and Maven:
```powershell
$MAVEN_VERSION = '3.3.9'
$JAVA_VERSION = '1.8.0_101'
$env:JAVA_HOME = "c:\java\jdk${JAVA_VERSION}"
$env:M2_HOME = "c:\java\apache-maven-${MAVEN_VERSION}"
$env:M2 = "${env:M2_HOME}\bin"
```

```cmd
if "%JAVA_VERSION%"=="" set JAVA_VERSION=1.8.0_101
set JAVA_HOME=c:\java\jdk%JAVA_VERSION%
if "%MAVEN_VERSION%"=="" set MAVEN_VERSION=3.3.9
set M2_HOME=c:\java\apache-maven-%MAVEN_VERSION%

```
```bash
JAVA_VERSION='1.8.0_121'
MAVEN_VERSION='3.3.9'

```
The runner script can also be used to launch individual forms that have been largely based on
examples from the Standard Widget Toolkit study
project [lshamsutdinov/study_swt](https://github.com/lshamsutdinov/study_swt),
which in turn is based on SWT examples from Jan Bodnar's [website](zetcode.com) as:

```shell
./run.sh [ConfigFormEx|ScrolledTextEx|ComplexFormEx]
```
### Toolbar Buttons

![launch](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/launch.png)
launches the browser

![launch](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/find.png)
injects the [SWD Element Searcher script](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/ElementSearch.js) into the page then
loops polling the page witing for user to select some element via right click and to fill and submit the form:
![SWD Table](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/swd_table.png)

The Java reads back the result once it available and adds a breadcrump button:
![breadcumps](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/breadcumps.png)

The breadcrump button opens the form dialog with the details of the element:
![form](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/form.png)

The save and load buttons
![flowchart](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/save.png)
![flowchart](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/open.png)
save  and restore the test session in YAML format.
![flowchart](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/open_sesssion.png)

The flowchart button
![flowchart](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/flowchart.png)

starts codegeneration using [Jtwig](http://jtwig.org/) tempate and `elementData` hash and opens result in a separate dialog:
![codegen](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/codegen.png)

The preferences.png button
![preferences](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/preferences.png)
opens the configuration dialog
![config](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/screenshots/config.png)
Currently the browser and template selection are configurable, one also can set the base URL.

There is also a demo button that executes these actions automatically (for one element):
![demo](https://github.com/sergueik/selenium_java/blob/master/swd_recorder/src/main/resources/demo.png)

Currently project is hardcoded to use Chrome browser on Windows os, and Firefox on the rest of platforms.
The YAML configuration will be fuly integrated shotly.
Eventually other common formats: YAML, JSON, POI or Java properties file - will be supported.


### Operation
Both __SWD__ and __SWET__ inject certain Javascript code `ElementSearch.js` into the page, that the user can interct with with the mouse right-click.
After injecting the script the IDE waits polling for the speficic
`document.swdpr_command` object to be present on that page. This object is created  by the `ElementSearch.js`
when user selects the specific element on the page he is interested to access in the test script,
and confirms the selection by entering the name of the element and clicking the 'Add Element' button.
The `document.swdpr_command` object will contain certain properties of the selected element:

* Absolute XPath, that looks like `/html/body/*[@id = "www-wikipedia-org"]/div[1]/div[1]/img[1]`
* Attribute-extended XPath that looks like `//a[@href="/script/answers"]/img[@src="question.png"]`
* Firebug-style cssSelector (all classes attached to all parent nodes), that look like `ul.nav-links li.nav-links__item div.central.featured.logo-wrapper > img.central.featured-logo`
* Element text (transalted under the hood into XPath `[contains()]` expression).
* Input for Angular Protractor-specific locators `repeater`, `binding`, `model`, `repeaterRow` etc. (WIP)
* Element ID (when available)

#### Automation of Locator Shortening
Auto-generated locators often become unnecessarily long, e.g. for the facebook logo one may get:
```css
div#blueBarDOMInspector > div._53jh > div.loggedout_menubar_container >
div.clearfix.loggedout_menubar > div.lfloat._ohe >
h1 > a > i.fb_logo.img.sp_Mlxwn39jCAE.sx_896ebb
```
Currently __SWET__ does not have an algorythm for shortening these locators.
Adding smart locator generators is a work in progress.

### Component Versions
As typical with Selenium, the __SWET__ application only runnable with the matching combination of versions of Selenium jar,
browser driver and browser itself is used - making __SWET__ work with the most recent releases of Selenium and browser drivers is not currently a priority.

Example of supported version combination is

|                      |              |
|----------------------|--------------|
| SELENIUM_VERSION     | __2.53.1__       |
| FIREFOX_VERSION      | __45.0.1__       |
| CHROME_VERSION       | __54.0.X__ |
| CHROMEDRIVER_VERSION | __2.24__         |


One can download virtually every old build of Firefox from
https://ftp.mozilla.org/pub/firefox/releases, and selected old builds of Chrome from
http://www.slimjetbrowser.com/chrome/, for other browsers the download locations vary.

This is why it may be worthwhile setting up Virtual Box e.g. [selenium-fluxbox](https://github.com/sergueik/selenium-fluxbox) to run the appliation with fixed downlevel browser versions.

### Safari Testing
If you have Mac OSX 10.12.X Sierra / Safari 10.X , then the Apple Safari driver would be installed automatically,
but it does not seems to work with Selenium __2.53__.
For earlier releases, you have to downgrade the Selenium version in the `pom.xml` to __2.48__
then follow the [Running Selenium Tests in Safari Browser](http://toolsqa.com/selenium-webdriver/running-tests-in-safari-browser).


### Code Templates

The code is generated using SWIG templates which look like
```
{#
template: Basic Page Objects (Java)
#}
class TestPage (Page) {
  // {{ ElementText }}
  {% if (ElementSelectedBy == 'ElementCssSelector') -%}
  @FindBy( how = How.CSS, using = "{{ ElementCssSelector }}" )
{% elseif (ElementSelectedBy == 'ElementXPath') -%}
  @FindBy( how = How.XPATH, using = "{{ ElementXPath }}" )
{% elseif (ElementSelectedBy == 'ElementId') -%}
  @FindBy( how = How.ID, using =  "{{ ElementId }}" )
{% endif -%}
{% if (ElementVariable != '') -%}
  private WebElement {{ ElementVariable }};
{% else -%}
  private WebElement element;
{% endif -%}
```
Any language/framework can be supported. The comment
```
{#
template: Basic Page Objects (Java)
#}
```
is reserved for future use, when tester is allowed to provide the path to template during session configuration.

### Configuration, saving and loading

__SWET__ may be saved the element locators in the YAML format, using [snakeyaml](https://bitbucket.org/asomov/snakeyaml). Example:
```yaml
version: '1.0'
created: '2017-02-21'
seleniumVersion: '2.53.1'

# Browser parameters
browser:
  name: firefox
  platform: linux
  version: '45.0.1'

# Browser parameters
browser:
  name: chrome
  platform: windows
  version: '54.0.2840.71'
  driverVersion: '2.24'
  driverPath: 'c:/java/selenium/chromedriver.exe'

# Selenium Browsers
browsers:
  - chrome
  - firefox

# Elements
elements:
  ce094429-d4bd-4eb0-83ab-6d10c563f456:
    ElementCssSelector: div[id = "page-body"] > div.main-container > section.main-content > div.main-content-right > div.row.highlight > section.card.titled > section.project-info > header > h3 > a
    ElementCodeName: 'sourceforge project link'
    Command: AddElement
    Caller: addElement
    ElementPageURL: https://sourceforge.net/
    CommandId: ce094429-d4bd-4eb0-83ab-6d10c563f456
    ElementStepNumber: 1
    ElementSelectedBy: ElementXPath
    ElementText: Staff Choice Outlook CalDav Synchronizer
    ElementId: ''
    ElementVariable: userSelectedVariableName
    ElementXPath: id("page-body")/div[1]/section[1]/div[2]/div[2]/section[1]/section[2]/header[1]/h3[1]/a[@href="/projects/outlookcaldavsynchronizer/?source=frontpage&position=1"]
```

### Work in Progress
* UI improvements adding more form elements
* Testing with Safari and variety of IE / Edge browsers
* Adding the code generator templates
* Codebase cleanup
* Adding Threads to Element Finder button

### Links

#### SWT

  * [main SWT snippets directory](https://www.eclipse.org/swt/snippets/)
  * [SWT examples on javased.com](http://www.javased.com/?api=org.eclipse.swt.widgets.FileDialog)
  * [SWT - Tutorial by Lars Vogel, Simon Scholz](http://www.vogella.com/tutorials/SWT/article.html)
  * [Opal Project (SWT new widgets library) by Laurent Caron](https://github.com/lcaron/opal)
  * [Nebula - Supplemental Widgets for SWT](https://github.com/eclipse/nebula)
  * [danlucraft/jruby-swt-cookbook](https://github.com/danlucraft/jruby-swt-cookbook)
  * [danlucraft/swt](https://github.com/danlucraft/swt)
  * [fab1an/appkit toolkit for swt app design](https://github.com/fab1an/appkit)
  * [SWT dependency repositories](http://stackoverflow.com/questions/5096299/maven-project-swt-3-5-dependency-any-official-public-repo)
  * [SWT jar ANT helper](http://mchr3k.github.io/swtjar/)
  * [Examples](http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/CatalogSWT-JFace-Eclipse.htm)
  * [Examples](https://github.com/ReadyTalk/avian-swt-examples)
  * [swt-bling](https://github.com/ReadyTalk/swt-bling)
  * [Multiplatform SWT](https://github.com/jendap/multiplatform-swt)
  * [SWT Single Jar Packager](https://github.com/mchr3k/swtjar)
  * [SWT custom preference dialog](https://github.com/prasser/swtpreferences)
  * [SWT/JFace Utilities](https://github.com/Albertus82/JFaceUtils)
  * [SWT/WMI](https://github.com/ctron/wmisample)
  * [SWT Tools](https://github.com/bp-FLN/SWT-Tools)
  * [SWT choice dialog customization](https://github.com/prasser/swtchoices)
  * [SWT Browser component based recorder](https://github.com/itspanzi/swt-browser-recorder-spike)
  * [Joptions Pane examples](http://alvinalexander.com/java/java-joptionpane-examples-tutorials-dialogs)

#### EClise Plugind
  * [java2s](http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/Eclipse-Plugin.htm)

#### Code Generation

  * [Jtwig](https://github.com/jtwig/jtwig)
  * [Thymeleaf](http://www.thymeleaf.org/)
  * [StringTemplate](http://www.stringtemplate.org/)

#### Selenium Locator Strategies

  * [Choosing Effective XPaths](http://toolsqa.com/selenium-webdriver/choosing-effective-xpath/)
  * [Use XPath locators efficiently](http://bitbar.com/appium-tip-18-how-to-use-xpath-locators-efficiently/)

#### YAML
  * [snakeyaml](https://bitbucket.org/asomov/snakeyaml)

#### Javascript injection
  * [Keymaster](https://github.com/madrobby/keymaster)

#### Misc.

  * [how to define conditional properties in maven](http://stackoverflow.com/questions/14430122/how-to-define-conditional-properties-in-maven)
  * [eclipse xwt](https://wiki.eclipse.org/XWT_Documentation)
  * [mono/xwt](https://github.com/mono/xwt)
  * [json2](https://github.com/douglascrockford/JSON-js)

### Note

[Swet](http://www.urbandictionary.com/define.php?term=swet&defid=6820405) - *a word that describes something that's hot. Or something that would typically take a lot of skill and practice to do, therefore causing the person to sweat*.

### Author
[Serguei Kouzmine](kouzmine_serguei@yahoo.com)

