Vyuudha - A clean room implementation of a distributed storage middleware for education and research.

Q: What does Vyuudha mean?

A: Vyuudha in sanskrit means "Distributed".

It is free software, licensed under the terms of the GNU General Public License.

Dynamic Distributed Storage
===========================

Implement a bare-metal Dynamo like Distributed storage middleware for educational and learning purpose.
The primary objective of this project will be to develop a middleware which is simple not production quality and used for understaing a distributed storage system. To achieve this the design should be very much flexible and have a pluggable architecture for various algorithms which can be implemented and used with this system.

Requirements: ((all listed in pom.xml) Java version: 1.6.0_22) 
-------------
    1. Apache Maven 2.2.1, 
    2. Google Guava r07, 
    3. log4j 1.2.16, 
    4. BDB Java Edition 3.3.75, 
    5. Google Protocol Buffer 2.3.0,
    6. jUnit 3.8.1

How to setup dev environment:
-----------------------------
    1. Install Maven plugin (M2Eclispe) for Eclipse
    2. Import the project as a Maven project: http://stackoverflow.com/questions/2061094/importing-maven-project-into-eclipse
    3. Import all the dependencies listed in pom.xml to your local Maven repo.


Important Notes:
----------------
	1. config/ should hold all the configuration meta-data for the respective node.
	2. store/ should hold the BDB flat files
	
Contact us!
-----------
    

More details to be updated!

