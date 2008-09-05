This example shows how to run mule-cics inside a J2EE compliant server.
In these examples, the client sends message to ActiveMQ JMS Queue (jms://TEST.FOO.)

Mule running in JBoss receives the message and sends back a dummy reply.

Pre-requisites:

1. Download mule-jca-2.0.1.rar from http://dist.codehaus.org/mule/distributions.
2. Delete mule-config.xml from mule-module-jca-core-2.0.1.jar (present in mule-jca-2.0.1.rar).
3. Delete mule-module-jca.xml from mule-module-jca-jboss-2.0.1.jar (present in the RAR).
4. Copy the RAR file jboss/lib folder.
5. Copy following jars in jboss/lib folder.
   a. cci.jar
   b. xbean.jar
   c. ccf2.jar
   d. cicseci.jar 
   e. cicsframe.jar
   f. ctgclient.jar
   g. activemq-core-4.1.1.jar
   h. mule-transport-cics-2.0.1.jar

Steps to deploy the example:

1. Edit build.properties file to specify path to JBoss and mule-cics.

2. >ant deploy
   This command will create dist/mule-ear-2.0.1.ear 
   copy the same file to ${JBOSS_HOME}/server/default/deploy directory.

3. Start Active MQ.

4. Restart JBoss.

5. Run client to send a message to ActiveMQ JMS queue named "TEST.FOO".

6. Please check JBoss console.
   The message should be received by Mule and dummy reply should be sent.
 
