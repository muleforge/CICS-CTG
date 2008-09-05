Simple example for mule-cics transport
------------------------------------------

This example describes a simple scenario, when a client can send an
XML message (over HTTP or JMS) to a Mule Server that hosts a mule-cics service.

--------------------------------------------
Steps to run the sample
--------------------------------------------

1. Start the server
   examples/simple/mule-server>run.bat

2. Run the client using Maven.
   example/simple/client>mvn test

The client will send an XML message to the server 

The mule-cics service does the following:
- transform received XML into fixed-length format. 
- Invoke mainframe program by sending fixed-length message. 
- Receive fixed-length reply from the mainframe.
- Transform fixed-length reply into XML.

The XML will be sent back to the client as response.

This sample does not actually connect to mainframe, instead a dummy
fixed-length response is read from filesystem.

To understand, how the mule-cics service is configured, please study
the following file.

- mule-server/config/mule-cics-config.xml


The mule-cics service can also be configured so that client can
connect via web-service or REST API. These are described in 
the examples/advanced folder.


