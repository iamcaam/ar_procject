

@echo off
CLS
ECHO.


:init
setlocal DisableDelayedExpansion
set "batchPath=%~0"
for %%k in (%0) do set batchName=%%~nk
set "vbsGetPrivileges=%temp%\OEgetPriv_%batchName%.vbs"
setlocal EnableDelayedExpansion

:checkPrivileges
NET FILE 1>NUL 2>NUL
if '%errorlevel%' == '0' ( goto gotPrivileges ) else ( goto getPrivileges )

:getPrivileges
if '%1'=='ELEV' (echo ELEV & shift /1 & goto gotPrivileges)
ECHO.


ECHO Set UAC = CreateObject^("Shell.Application"^) > "%vbsGetPrivileges%"
ECHO args = "ELEV " >> "%vbsGetPrivileges%"
ECHO For Each strArg in WScript.Arguments >> "%vbsGetPrivileges%"
ECHO args = args ^& strArg ^& " "  >> "%vbsGetPrivileges%"
ECHO Next >> "%vbsGetPrivileges%"
ECHO UAC.ShellExecute "!batchPath!", args, "", "runas", 1 >> "%vbsGetPrivileges%"
"%SystemRoot%\System32\WScript.exe" "%vbsGetPrivileges%" %*
exit /B

:gotPrivileges
setlocal & pushd .
cd /d %~dp0
if '%1'=='ELEV' (del "%vbsGetPrivileges%" 1>nul 2>nul  &  shift /1)


::START


sc query "AcroRedAgentService" | find "RUNNING"
if "%ERRORLEVEL%"=="0" (
    
    sc config "MSiSCSI"  start= auto
    net start MSiSCSI
    msiexec /uninstall {794F3509-B1B1-4CC0-85E6-5C4F2B51555D} /passive
    msiexec /uninstall {8DFBFCC9-B552-4D9B-8787-192DD00463D1} /passive
    msiexec /uninstall {E8F23499-77B5-4194-B6D3-114859B68B0A} /passive
    msiexec /uninstall {178620C2-6BB1-412E-9A70-7BA0AF5C4966} /passive
    msiexec /uninstall {404E5FC0-0DD7-4E8D-B423-E7E2E2C3B09D} /passive
	msiexec /uninstall {082CD505-CE17-4562-936B-3953FD401D0B} /passive
	msiexec /uninstall {C95BF7FD-BD1B-40FB-BC27-50C75F1C4FE1} /passive	
	
	
    bin\setup.exe
	


) else (
    
    sc config "MSiSCSI"  start= auto
    net start MSiSCSI
    bin\setup.exe

)

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::