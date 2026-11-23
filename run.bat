@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
for /r src\main\java %%f in (*.java) do echo "%%f">> sources.txt
javac -encoding UTF-8 -d out @sources.txt
if errorlevel 1 (
  del sources.txt 2>nul
  echo No se pudo compilar. Comprueba que JDK 17 o superior este instalado.
  pause
  exit /b 1
)
del sources.txt 2>nul
java -cp out duelo.Main
