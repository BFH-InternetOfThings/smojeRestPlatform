#!/bin/bash
su - pi -c "git -C /home/pi/smoje/smojeRestPlatform/ pull"
su - pi -c "mvn -C /home/pi/smoje/smojeRestPlatform/ install"
chmod +x /home/pi/smoje/smojeRestPlatform/script/updateAndStartSmojeSensorApp.sh
java -Djava.library.path="/usr/lib/jni/" -jar /home/pi/smoje/smojeRestPlatform/target/piservice-jar-with-dependencies.jar