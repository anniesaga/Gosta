#!/usr/bin/env bash

if [ "$(uname)" == "Darwin" ]; then
   javac -cp servlet-api.jar:org.json.jar WEB-INF/classes/servlets/Companies.java && java -jar winstone.jar WEB-INF/lib/org.json.jar --webroot .
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
   javac -cp server/servlet-api.jar:server/org.json.jar server/WEB-INF/classes/servlets/Companies.java && java -jar server/winstone.jar server/WEB-INF/lib/org.json.jar --webroot server
elif [ "$(expr substr $(uname -s) 1 6)" == "CYGWIN" ]; then
    javac -cp "servlet-api.jar;org.json.jar" WEB-INF/classes/servlets/Companies.java && java -jar winstone.jar WEB-INF/lib/org.json.jar --webroot
fi
