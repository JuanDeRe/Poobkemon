Para compilar los archivos de dominio y presentacion, vamos a la carpeta raiz en la consola y ejecutamos

javac -d bin -cp lib\* src\domain\*.java src\presentation\*.java

Para compilar los test con sus librerias usamos

javac -d bin -cp "bin;lib\*" src\test\*.java

para correr el prgrama usamos el comando

java -cp "bin;lib\*" src.presentation.PoobkemonGUI

o desde el jar

java -jar PoobkemonGUI.jar



