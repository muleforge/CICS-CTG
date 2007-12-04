@echo off
REM -------------------------------
REM Parameters: %1 - directory containing XSD files.
REM %2 - directory for output JAR files.
REM -------------------------------

if "%2"=="" echo Parameter missing. Usage: tmp inputDir outputDir& goto :eof

SET XBEANS_DIR=d:\tools\xmlbeans-2.1.0\bin

FOR %%F IN (%1\*.xsd) DO %XBEANS_DIR%\scomp -out %2/%%~nF.jar %%F
