@echo off
setlocal
if not exist out mkdir out
javac -d out src\main\java\com\gridweaver\model\*.java src\main\java\com\gridweaver\engine\*.java src\main\java\com\gridweaver\*.java
if errorlevel 1 exit /b 1
java -cp out com.gridweaver.Main
endlocal
