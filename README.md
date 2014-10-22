javafx8-gradient-builder
========================

Gradient Builder application for JavaFX 8

To run this application/code you should have JDK 8 installed on your machine.

CASE#1:
If your machine consists of both JDK 7 and JDK 8, and if the Env variable "JAVA_HOME" points to JDK 7, create a new Env varaible "JAVA_1_8_HOME" which points to JDK 8 installation directory and build the project.

CASE#2:
If the Env variable "JAVA_HOME" points to JDK 8, change the env varaible to "JAVA_HOME" in <executable/> in the pom.xml and build the project.

The changed code in pom.xml should look like below:
	<executable>${JAVA_HOME}/bin/javac</executable>
