@echo off
setlocal enableextensions

set SCRIPT=%0
set DQUOTE="

:: Detect how script was launched
@echo %SCRIPT:~0,1% | findstr /l %DQUOTE% > NUL
if %ERRORLEVEL% EQU 0 set PAUSE_ON_CLOSE=1

:: Run your app
java -jar ServerSpaceReports.jar

:EXIT
if defined PAUSE_ON_CLOSE pause