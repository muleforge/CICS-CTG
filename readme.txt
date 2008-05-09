-------------------------------------------------------------------------
Steps to deploy/run
-------------------------------------------------------------------------
1. svn co https://seriola.vertexsoft.com/svn/seriola/products/trunk/mule-cics

2. Edit build.xml and specify ${mule.home}

3. Build mule-cics-transport-2.0.0.jar using mvn.
   ${mule-cics}/core>mvn package

4. Copy target/mule-transport-cics-2.0.0.jar to ${mule.home}/lib/mule
   Copy xbean.jar (Apache XMLBeans) to ${mule.home}/lib/opt
 
5. Start ActiveMQ.
 
6. ./run.sh
    This will start the mule server using config/mule-seriola-config.xml
 
7. Send a sample-command XML message from seriola client.
   Mule will receive the msg and invoke
    - org.mule.providers.stubtest.XmlMessageDispatcher.send()
      XmlMessageDispatcher will use the condition file set in 
      mule-seriola-config.xml and send back dummy reply.   
-------------------------------------------------------------------------
