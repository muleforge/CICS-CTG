Client sends message to Mule by GET method.
The URL parameters are converted into the XML on the server side.

Pre-requisites:
Download the following JARS in ${rest}/client-wadl/lib folder.
(These jars are present in Web Application Description Language (WADL) project at Java.net.The URL for WADL project is 'https://wadl.dev.java.net/')

activation-1.1.jar             
localizer-1.0.jar
comresrcgen-1.0.jar            
wadl-ant-1.0-SNAPSHOT.jar
jaxb-api-2.0.jar               
wadl-cmdline-1.0-SNAPSHOT.jar
jaxb-impl-2.0.3.jar            
wadl-core-1.0-SNAPSHOT.jar
jaxb-xjc-2.0.3.jar             
xercesImpl-2.7.1.jar
jsr173_api-1.0.jar

Steps to run the example:

1. Start Mule Server.

2. >ant compile
   This command will generate the stubs from WADL file which is located at
   "http://localhost:7777/seriola/CustomerInterface?wadl".

3. >ant run
    The above command will send the message to Mule server. It should receive 
    the response back from the server.









 
