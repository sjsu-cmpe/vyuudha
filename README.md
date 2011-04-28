Vyuudha - A Dynamic Distributed Store
=====================================
A clean room implementation of a distributed storage middleware for education and research. It's 
primary objective is to implement a bare-metal distributed storage middleware for educational and 
learning purpose. The middleware is intended to understand the dynamics and anatomy of a distributed
storage system and is not production quality system.
To achieve this the design should be flexible and have a pluggable architecture for various algorithms 
which can be implemented and used with this system.

The application has a pluggable architecture where the following functionalities/algorithms can be configured:  

1. Server (BIO/NIO): `com.dds.server.*`
2. Persistent store mechanism (plug in BDB, MongoDB, MySQL etc): `com.dds.plugin.storage.*`
3. Routing mechanism (Consistent hashing/vBuckets): `com.dds.plugin.routing.*`
4. Hashing function: `com.dds.plugin.hashing.*`
5. Serialization format (Client side and node-to-node communication): `com.dds.plugin.serialization.*`  
6. Membership: `com.dds.plugin.membership.*`

Q: What does `Vyuudha` mean?  
A: Vyuudha in sanskrit means `Distributed`. Prounounced "vy-uudh".  

Requirements:  
-------------
1. All listed in pom.xml.
2. Java version: 1.6.0_22
    
    
For dependecies which are not found in maven, then do this:  
1. Download the jar file from the web.  
2. mvn install:`install-file -Dfile=location/jar_name.jar -DgroupId=group_id_here -DartifactId=artifact_id_here -Dversion=version_number_here -Dpackaging=jar`  
3. The jar file will be installed in your local maven repo.  
    
    
How to setup dev environment:
-----------------------------
1. Install Maven plugin (M2Eclispe) for Eclipse
2. Import the project as a Maven project: http://stackoverflow.com/questions/2061094/importing-maven-project-into-eclipse
3. Import all the dependencies listed in pom.xml to your local Maven repo.

Project Details:
---------------
1. The project has upto 20 packages. The main packages in the project are structured in the following way.
 - client : Contains the client code. This is the API package which is provided to a client.
 - cluster : The code which maintains the cluster information is present here.
 - core : The routing, storage and request handlers, which are core to the system and un-pluggable, are present here.
 - exception : All Vyuudha-defined exceptions are contained in exception package.
 - interfaces : All pluggable entity interfaces are stored in this package.
 - plugin : This is the key package. All pluggable entity classes are in the sub-packages under plugin package. To add
 a plugin for routing, create a new file under com.dds.plugin.routing package.
 - server : This package contains the NIO/BIO server implementations for client request handling server and the server
 to handle internal replication and routing requests.
 - utils : This package contains the helper classes.
 
2. The pluggable entities of the project
 - APIInterface : This interface is used to create new storage engines. Currently MySQL, BDB and MongoDB have been 
 implemented. To implement a new storage engine, implement APIInterface and use the required functions. Functions 
 that need not be defined throw the Vyuudha-custom Unsupported Exception. This interface is also implemented by the
 client handler.
 - HashingInterface : This interface is used to create new hash function classes. Currently Modulo Hash Function and 
 Murmur Hash Function are implemented. To use a different/new hash function, create a new class under comd.dds.plugin.hashing
 package and implement HashingInterface inside it. 
 - MembershipInterface : This interface contains the classes that are used for membership detection in the cluster. The
 current implementations are AtomicBroadcast and GossipProtocol. The implementations for which use this interface are
 under com.dds.plugin.membership package.
 - RoutingInterface : This interface contains the classes that are useful in routing requests between the nodes. The
 current implementation are ConsistentHashing and VBuckets. The classes which implement RoutingInterface are declared 
 under com.dds.plugin.routing package.
 - ServerInterface : This interface is currently implemented for NIO and BIO servers. All implementations for a new type
 of server service should implement ServerInterface. The implementation classes are under com.dds.server package.
 
3. To create a new plugin
 - Depending upon the functionality, implement one of the interfaces listed above. The interfaces can also be found
 in com.dds.interfaces package.
 - Locate the implementation package for the particular interface. com.dds.plugin.<InterfaceModule>. Create the new 
 implementation class under this package.
 - Open server.properties file.
 - Under the Pluggable modules section, choose the interface module that needs to be edited. The value for the pluggable 
 module should be the entire class path of the new class created. example, for a new class implemented under membership,
 "membership=com.dds.plugin.membership.newMembershipModule" 
 - Save.

4. Properties/Config files
 There are 3 properties files and 1 xml file which are used by the project.
 - server.properties : This file contains the server identification id or nodeId. This file also contains the various
 pluggable modules which the project will use. 
 - database.properties : This file contains the storage engine information, such as store names, logon information and
 path. The number of writes (replication factor) is also declared in this file. 
 - replication.properties : This properties file contains the information regarding the storage engine components for
 replication purpose.  
 - nodes.xml : This file contains the startup information regarding all the nodes in the cluster. The nodeId respective
 to each node on the cluster, its IP and the ports which can be used on it are listed in this file. The replication
 and routing server/ip information is also listed here. The initial nodes configuration SHOULD BE listed in this file
 unless if it is a single instance node. (For single instance details check Starting and Stopping Vyuudha)

5. Starting and Stopping the Vyuudha
 - There are two ways of starting the server.
 
 a. Using the start script provided under scripts folder present in the project directory.
 
 b. Run InitDDS using an IDE
 
 - To stop the server.
 
 a. Use the stop script inside the scripts folder present in the project directory.
 
 b. If server has been started using an IDE, then simply end the execution.
 
 - To start a single instance server (Only one node is up and running)
 
 a. Open server.properties.
 
 b. Locate the field "single_instance".
 
 c. To run a single instance, set single_instance value as true.
 
 d. To run multiple instance, confirm that nodes.xml has been populated and updated and set single_instance to false
 

Important Notes:
----------------
1. config/ should hold all the configuration meta-data for the respective node.
2. store/ should hold the BDB flat files
	
Contact us!
-----------


License
-------
It is free software, licensed under the terms of the GNU General Public License.

