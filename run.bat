@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -sourcepath src\main\java -d out src\main\java\duelo\Main.java
if errorlevel 1 (
  echo No se pudo compilar. Comprueba que JDK 17 o superior este instalado.
  pause
  exit /b 1
)
java -cp out duelo.Main

