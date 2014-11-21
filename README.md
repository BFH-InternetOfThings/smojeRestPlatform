smojeRestPlatform
=================

Build/Install
--------------





mvn clean package



The Pi4J library will need root privileges in order to control the GPIO pins bad thing...

edit /etc/default/tomcat7 and set the following to root:

TOMCAT7_USER=root
TOMCAT7_GROUP=root

add java opts parameter -Djava.library.path=/home/pi/smoje/lib in the same file

create folder on your raspberry /home/pi/smoje/cam
create folder on your raspberry /home/pi/smoje/lib

put libpi4j.so from the pi4j-core jar into the folder /home/pi/smoje/lib

Standard start url: http://localhost:8080/piservice/api/smoje/sensors/


------ bugs -------

sometimes: A MultiException has 2 exceptions. They are: 1. java.lang.NoClassDefFoundError: Could not initialize class ch.bfh.iot.smoje.raspi.SmojeService 2. java.lang.IllegalStateException: Unable to perform operation: create on ch.bfh.iot.smoje.raspi.SmojeService occurs

workaround: service tomcat7 restart

