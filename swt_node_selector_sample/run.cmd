@echo OFF
:setlocal
set SKIP_TEST=tue
if "%TOOLS_DIR%"=="" set TOOLS_DIR=c:\java
if "%JAVA_VERSION%"=="" set JAVA_VERSION=1.8.0_101
if "%JAVA_HOME%"=="" set JAVA_HOME=%TOOLS_DIR%\jdk%JAVA_VERSION%
set JAVA_OPTS=-Xms256m -Xmx512m
if "%MAVEN_VERSION%"=="" set MAVEN_VERSION=3.5.0
if "%M2_HOME%"=="" set M2_HOME=%TOOLS_DIR%\apache-maven-%MAVEN_VERSION%
if "%M2%"=="" set M2=%M2_HOME%\bin
set MAVEN_OPTS=-Xms256m -Xmx512m
PATH=%JAVA_HOME%\bin;%M2%;%PATH%
set TARGET=%CD%\target
set PACKAGE_NAME=swt_node_selector_sample
set PACKAGE_VERSION=0.0.1-SNAPSHOT
set MAIN_APP_PACKAGE=example
set MAIN_APP_CLASS=%1
if NOT "%MAIN_APP_CLASS%" == "" shift
if "%MAIN_APP_CLASS%"=="" set MAIN_APP_CLASS=SimpleToolBarEx
set APP_HOME=%CD:\=/%
REM 
REM will be mvn.bat or mvn.md
 
if "%SKIP_TEST%"=="" (
REM Test
call mvn test
REM Compile
call mvn clean package install
) else (
REM compile
call mvn -Dmaven.test.skip=true clean package install
)

REM Run
REM NOTE: shift does not affect the value of %*
REM passsing the log4j configuration seems to be have no effect
REM  -Dlog4j.configuration=file:///%APP_HOME%/src/main/resources/log4j.properties ^

java ^
  -cp %TARGET%\%PACKAGE_NAME%-%PACKAGE_VERSION%.jar;%TARGET%\lib\* ^
  %MAIN_APP_PACKAGE%.%MAIN_APP_CLASS% ^
  %1 %2 %3 %4 %5 %6 %7 %8 %9

:endlocal
goto :EOF
