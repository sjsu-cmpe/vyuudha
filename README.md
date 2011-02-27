Vyuudha - Think Distributed
===========================
A clean room implementation of a distributed storage middleware for education and research.
The application has a pluggable architecture where the following functionalities/algorithms can be configured:  
    1. Server (BIO/NIO): `com.dds.interfaces.server.*`
    2. Persistent store mechanism (plug in BDB, MongoDB, MySQL etc): `com.dds.interfaces.storage.*`
    3. Routing mechanism (Consistent hashing/vBuckets): `com.dds.interfaces.routing.*`
    4. Hashing function: `com.dds.interfaces.hashing.*`
    5. Serialization format (Client side and node-to-node communication): `com.dds.interfaces.serialization.*`

Q: What does Vyuudha mean?  
A: Vyuudha in sanskrit means "Distributed". Prounounced "vy-uudh".  

Vyuudha - Dynamic Distributed Storage
=====================================

Implement a bare-metal Distributed storage middleware for educational and learning purpose.
The primary objective of this project will be to develop a middleware which is simple not production quality and used for understaing a distributed storage system. To achieve this the design should be very much flexible and have a pluggable architecture for various algorithms which can be implemented and used with this system.

Requirements:  
-------------
    1. All listed in pom.xml.
    2. Java version: 1.6.0_22
    
    
    For dependecies which are not found in maven, then do this:
    1. Download the jar file
    2. mvn install:install-file -Dfile=location/jar_name.jar -DgroupId=group_id_here -DartifactId=artifact_id_here -Dversion=version_number_here -Dpackaging=jar
    3. The jar file will be installed in your local maven repo.
    
    
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
    

License
-------
It is free software, licensed under the terms of the GNU General Public License.

