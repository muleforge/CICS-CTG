This example shows how to invoke mainframe programs by sending SOAP messages 
via JMS transport to the Mule ESB.

This example uses:
- The Axis2 web services library (JMS transport)
- ActiveMQ JMS server.
- Mule ESB with cics transport to invoke mainframe program. 

Steps to run the client:
1. Start mule server.

2. Start Active MQ.

3. Test client 
   ${AXIS2-JMS.HOME}>mvn test

   (The above command will execute the JUnit test case MuleCicsWithJMSTest with some default values of arguments(for e.g: ConnectionFactory name,Queue name etc.).You can change the default values of the arguments in pom.xml) 
