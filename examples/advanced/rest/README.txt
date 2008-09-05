+--------------------------------------------+
|    mule-cics examples using REST           |
+--------------------------------------------+

These example shows how clients can connect to mainframe (CICS) programs 
using REST(Representational State Transfer) API via the Mule ESB.

The steps are as follows:
- Configure a service in Mule that uses the CICS transport.
- The Mule service will make a WADL(Web application desciption language,similar to WSDL) available to the clients.
- Clients use the WADL to generate client-side stub.
- Client send requests to Mule using generated stub by either HTTP GET or POST method.
- Mule dispatches the request to the mainframe (CICS) program.
- The mainframe response is sent back to the WADL client.

This example consists of -
   1. Mule server  
   2. WADL clients- using java and ruby.

Mule Server:
   The mule-server directory contains the Mule server configuration files
and the script to start the Mule server.

Clients:

The following clients are available.

client-wadl  (using java language)
client-wadl-ruby (using ruby language)
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
| Running REST clients |
+----------------------+
1. client-wadl
1.1 Refer the ReadMe in client-wadl folder.
        
2. client-wadl-ruby
2.1 Refer ReadMe in client-wadl-ruby folder.
 
Note: Please refer http://www.ruby-lang.org/en/ for more details about ruby language.
