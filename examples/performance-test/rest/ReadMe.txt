+--------------+
|  Bench test  |
+--------------+

This example shows how to stress test CICS transport using REST interface.
The client sends requests to the mule server.The time taken for mule server
for diffrent no of iterations,no of threads can be calculated using this 
example. 

+---------------------+
| Running the example |
+---------------------+
Steps to start the server:
 ${bench}/rest/server#run.bat

Steps to run the client:
 ${bench}/soap/client#ant run10
 ( Runs 'perf-test-command10' with 10 iterations in single thread )
 OR
 ${bench}/soap/client#ant run50
 ( Runs 'perf-test-command50' with 10 iterations in single thread )
 OR
 ${bench}/soap/client#ant run100
 ( Runs 'perf-test-command100' with 10 iterations in single thread )

 OR
 ${bench}/soap/client#ant run500
 ( Runs 'perf-test-command500' with 10 iterations in single thread )

Note: The number of threads or number of iterations can be configured in 
build.xml 
