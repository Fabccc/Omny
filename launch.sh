#!/bin/bash

webserver="webserver.jar"
main_jar="webengine-0.0.1-jar-with-dependencies.jar"

# If the file already exists, remove it
if [ -f "$webserver" ]; then
    rm $webserver
fi

mvn clean install -DskipTests
cp ./webengine/target/$main_jar ./webserver.jar
java -jar $webserver