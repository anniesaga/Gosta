#!/usr/bin/env bash

if [ "$(uname)" == "Darwin" ]; then
    javac -cp servlet-api.jar:org.json.jar webroot/WEB-INF/classes/servlets/Companies.java && java -jar winstone.jar webroot/WEB-INF/lib/org.json.jar --webroot webroot        
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    javac -cp servlet-api.jar:org.json.jar webroot/WEB-INF/classes/servlets/Companies.java && java -jar winstone.jar webroot/WEB-INF/lib/org.json.jar --webroot webroot
elif ["$(expr substr $(uname -s) 1 6)" == "CYGWIN"]; then
    javac -cp "servlet-api.jar;org.json.jar" webroot/WEB-INF/classes/servlets/Companies.java && java -jar winstone.jar webroot/WEB-INF/lib/org.json.jar --webroot webroot
fi