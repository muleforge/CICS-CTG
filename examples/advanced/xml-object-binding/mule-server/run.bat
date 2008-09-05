@echo off
cls
setlocal
REM There is no need to call this if you set the MULE_HOME in your environment properties
if "%MULE_HOME%" == "" SET MULE_HOME=d:\tools\mule-2.0.1
if "%MULE_BASE%" == "" SET MULE_BASE=%MULE_HOME%

REM Any changes to the files in .\config will take precedence over those deployed to %MULE_HOME%\lib\user
SET MULE_LIB=.\

call "%MULE_BASE%\bin\mule.bat" -config mule-cics-jms-config.xml
