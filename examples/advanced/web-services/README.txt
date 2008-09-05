+--------------------------------------------+
| mule-cics examples using web-services API  |
+--------------------------------------------+

These example shows how clients can connect to mainframe (CICS) programs 
using web-service API via the Mule ESB.

The steps are as follows:
- Configure a service in Mule that uses the CICS transport.
- The Mule service will make a WSDL available to the clients.
- Clients use the WSDL to generate client-side stub.
- Client send SOAP requests to Mule using generated stub.
 (on HTTP or JMS transport).
- Mule dispatches the SOAP request to the mainframe (CICS) program.
- The mainframe response is sent back to the SOAP client.

This example consists of -
   1. Mule server  
   2. Web service clients- developed using third party packages (axis2,cxf etc.)

Mule Server:
   The mule-server directory contains the Mule server configuration files
and the script to start the Mule server.

Clients:

The following clients are available.

Axis2 using JMS  (under web-services/client-axis2-jms folder)
Axis2 using HTTP (under web-services/client-axis2 folder)
CXF using HTTP   (under web-services/client-cxf folder)
Ruby using HTTP  (under web-services/client-axis2-jms folder)
    The source code and build scripts are present in the client-XXXX folder.

+----------------------+
| Pre-requisites       |
+----------------------+
First make sure that you have installed MULE server on your machine.
Copy the mule-transport-cics.jar in ${MULE-HOME}/lib/mule directory.

+----------------------+
| Run MULE server      |
+----------------------+

Edit the path of MULE_HOME in run.bat or make sure you have set the
MULE_HOME environment variable as recommended in INSTALL.txt of MULE.
${mule-server}#run.bat

Alternatively, if you have added Mule to your executable path 
, you can run the example from the command line as follows:

Linux / Unix
------------
${mule-server}#mule -config mule-cics-soap-config.xml

Windows
-------
${mule-server}#mule.bat -config mule-cics-soap-config.xml

+----------------------+
| Running SOAP clients |
+----------------------+
1. Axis2 client (uses SOAP over HTTP)
1.1 Compile and Run 
    ./client-axis2#mvn test
        
2. CXF client (uses SOAP over HTTP)
   2.1 Compile and Run 
       ./client-cxf#mvn test

3. Ruby client (uses SOAP over HTTP)
   3.1 Compile and Run 
       ./client-ruby-soap/src#ruby SeriolaClient.rb
      
Note: Please refer http://www.ruby-lang.org/en/ for more details about ruby language.

4. Axis2 JMS client (uses SOAP over JMS)
   4.1 Compile and Run
       ./client-axis2-jms/src#mvn test
