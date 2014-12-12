#!/bin/bash
su - pi -c "git -C /home/pi/smoje/smojeRestPlatform/ pull"
su - pi -c "mvn -C /home/pi/smoje/smojeRestPlatform/ install"