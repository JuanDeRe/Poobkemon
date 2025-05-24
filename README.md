Para compilar se usa
javac -d bin -sourcepath src src/domain/*.java src/presentation/*.java

para crear el jar se usa
jar cvfm Poobkemon.jar Manifest.txt -C bin .

Para correr el juego se ejecuta el comando
java -jar Poobkemon.jar

Para correr las pruebas

java -jar lib/junit-platform-console-standalone-1.8.1.jar --class-path bin --scan-class-path