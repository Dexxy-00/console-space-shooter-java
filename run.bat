@echo off
cd /d %~dp0

cd src

echo Compiling...
javac -cp ".;..\lib\*" com/game/spaceshooter/*.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Running...
java -cp ".;..\lib\*" com.game.spaceshooter.Main

pause