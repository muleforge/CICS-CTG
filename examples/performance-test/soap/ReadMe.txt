+--------------+
|  Bench test  |
+--------------+

This example shows how to stress test CICS transport with SOAP transport.
The client sends multiple requests to the mule server in the no of threads
specified by the user in maven script.

+---------------------+
| Running the example |
+---------------------+
Steps to start the server:
 ${bench}/soap/mule-server#run.bat

Steps to run the client:
 1. Edit the configuration in ${bench}/soap/client-axis2/pom.xml.
    for e.g.
    ...
    ...
    <no-of-records>50</no-of-records>
    <no-of-threads>1</no-of-threads>
    <no-of-iterations>10</no-of-iterations> 
    ...
    ...
    
    The above configuration will execute the perf-test-command50 10 times in a single thread.


 
