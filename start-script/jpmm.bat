@echo off
setlocal

rem Set the directory where the JAR file is located
set "jarDirectory=%~dp0"

rem Find the JAR file with the specified name pattern
for %%F in ("%jarDirectory%\jpmm-*.jar") do (
    echo Running %%F...
    java -jar "%%F"
)

endlocal
