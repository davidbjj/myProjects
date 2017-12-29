QuickFix-Connector-Beta-v1.0
==============================
this proyect are build with the follow techniologies: 
- Spring boot
- Java 8
- Infinispan
- Apache Spark
- Junit
- Maven


Users Fix configuration:
=========================
- \resrources\config\sender.cfg
      inside sender.cfg you must provide the fix user.
      
      ....
      [SESSION]
		BeginString=FIXT.1.1
		DefaultApplVerID=FIX.5.0SP2
		SenderCompID="my fix user"
		TargetCompID=STUN
		SocketConnectPort=8162
	  ....

- \resources\users.properties
	  It need be changed the user into this file.
	  
	  user=my fix user
	  password=my fix password
	  userfix=my fix user
	  servername=STUN
	  
	  
Build and Run
==============
for run the proyect you must run the next instructions:
- mvn clean install 


!! DO NOT DELETE application.properties file it will be used for spring to cached the data.