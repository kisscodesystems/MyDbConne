Please download the source and compile it or download the jar from
https://github.com/kisscodesystems/MyDbConns/blob/master/MyDbConns.jar
Please move MyDbConns.jar into a safe folder.
Open a command line and navigate into that folder.
Type "java -version" to see the actual version of your java. (min 1.8 is required.)
See that the "java" command points to the correct java executable!
Example command to start this application:
java -cp /opt/OracleJdbc.jar:/opt/MyDbConns.jar com.kisscodesystems.MyDbConns.MyDbConnsMain
(Please change the folder of the database driver and the place of the MyDbConns.jar into your folders.)
Type "java -jar MyDbConns.jar ?" for the usable commands.
Type "java -jar MyDbConns.jar help" for more detailed information.
Type "java -jar MyDbConns.jar application story" if you want to read.
Type "java -jar MyDbConns.jar application describe" if you would like to get more details.
