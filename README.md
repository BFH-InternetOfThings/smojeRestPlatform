smojeRestPlatform
=================

Build/Install
--------------

mvn clean package

Install generated target/piservice.war to Tomcat/Jetty etc.

configure your tomcat:

The Pi4J library will need root privileges in order to control the GPIO pins bad thing...

edit /etc/default/tomcat7 and set the following to root:

TOMCAT7_USER=root
TOMCAT7_GROUP=root

add java opts parameter -Djava.library.path=/home/pi/smoje/lib in the same file

create folder on your raspberry /home/pi/smoje/camera
create folder on your raspberry /home/pi/smoje/lib

put libpi4j.so from the pi4j-core jar into the folder /home/pi/smoje/lib

Standard start url: http://localhost:8080/piservice/api/smoje/sensors/

