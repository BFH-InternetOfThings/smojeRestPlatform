#!/bin/bash
sh -c 'cd /home/pi/smoje/smojeRestPlatform/ && git pull'
sh -c 'cd /home/pi/smoje/smojeRestPlatform/ && mvn install'
chown -R pi /home/pi/smoje/
chmod +x /home/pi/smoje/smojeRestPlatform/scripts/updateAndStartSmojeSensorApp.sh
java -Djava.library.path="/usr/lib/jni/" -jar /home/pi/smoje/smojeRestPlatform/target/piservice-jar-with-dependencies.jar