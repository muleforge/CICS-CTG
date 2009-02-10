
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Bench test for JMS transport 
 */
public class BenchTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public BenchTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(BenchTest.class);
	}
	
	private String readRequestFile(String fileName){
		StringBuffer contents = new StringBuffer(); 
		InputStream iStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		BufferedInputStream bufferedInput = null;
        byte[] buffer = new byte[1024];
        try {
            //Construct the BufferedInputStream object
            bufferedInput = new BufferedInputStream(iStream);
            int bytesRead = 0;
            //Keep reading from the file while there is any content
            //when the end of the stream has been reached, -1 is returned
            while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                //Process the chunk of bytes read
                //in this case we just construct a String and print it out
                String chunk = new String(buffer, 0, bytesRead);
                contents.append(chunk);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedInputStream
            try {
                if (bufferedInput != null)
                    bufferedInput.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("-----------------------Resuest XML----------------------------");
        System.out.println("Resuest XML->"+contents);
        return contents.toString();
	}
	
	/**
     * Rigourous Test :-)
     */
	
	public void test1() throws Exception {
		 try {
			int noOfThreads = Integer.parseInt(System.getProperty("NO_OF_THREADS"));
			System.out.println("NO_OF_THREADS : "+noOfThreads);
			int noOfIterations = Integer.parseInt(System.getProperty("NO_OF_ITERATIONS"));
			System.out.println("NO_OF_ITERATIONS : "+noOfIterations);
			int noOfRecords = Integer.parseInt(System.getProperty("NO_OF_RECORDS"));
			System.out.println("NO_OF_RECORDS : "+noOfRecords);
			String verbose = System.getProperty("VERBOSE");
			System.out.println("VERBOSE : "+verbose);
			
			String msgText = null;
			switch (noOfRecords) {
				case 10:
					msgText = readRequestFile("perf-test-command10-request.xml");
					break;
				case 50:
					msgText = readRequestFile("perf-test-command50-request.xml");
					break;
				case 100:
					msgText = readRequestFile("perf-test-command100-request.xml");
					break;
				case 500:
					msgText = readRequestFile("perf-test-command500-request.xml");
					break;
				default:
					break;
			}
			long total_start = System.currentTimeMillis();
			for (int i = 0; i < noOfThreads; i++) {
				TestPerformance test = new TestPerformance();
				test.setNoOfIterations(noOfIterations);
				test.setNoOfRecords(noOfRecords);
				test.setMessageText(msgText);
				//Set logging
				if(verbose.equalsIgnoreCase("true")){
					test.setVerbose(true);
				}
				Thread thread = new Thread(test);
				thread.start();
				thread.join();
			}
			
			long total_end = System.currentTimeMillis();
			System.out.println("-------------------------------------------------------------");
			long total_time = total_end - total_start;
	        double throughput = (noOfIterations*noOfThreads)*1000.0/total_time;

	        System.out.println("benchmark takes "+total_time/1000 + "." + total_time%1000+" seconds");
	        System.out.println("throughput "+throughput);

	        System.out.println("-------------------------------------------------------------");
		}catch (Exception e) {
			System.out.println("Exception in BenchTest");
			e.printStackTrace();
		}
	}
	
	
	
	public class TestPerformance implements Runnable {
		private int noOfRecords;
		private int noOfIterations=1;
		private Connection connection = null;
		private Session session = null;
		private MessageProducer sender = null;
		private boolean verbose = false;
		private String messageText;
		
		public TestPerformance() throws Exception {
			init();
		}
		
		private void init() throws Exception {
			Properties props = new java.util.Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
			Context jndiContext = new InitialContext(props);
			QueueConnectionFactory factory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
			connection = factory.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination sendDestination = session.createQueue("TEST.FOO");
			sender = session.createProducer(sendDestination);
			connection.start();
		}
		
		public String getMessageText() {
			return messageText;
		}

		public void setMessageText(String messageText) {
			this.messageText = messageText;
		}

		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}

		public int getNoOfIterations() {
			return noOfIterations;
		}

		public void setNoOfIterations(int noOfIterations) {
			this.noOfIterations = noOfIterations;
		}
		
		public int getNoOfRecords() {
			return noOfRecords;
		}

		public void setNoOfRecords(int noOfRecords) {
			this.noOfRecords = noOfRecords;
		}

		public void run() {
			if(verbose){
				System.out.println("In run method");
			}
			for(int i=0;i<noOfIterations;i++){
				switch( noOfRecords ){
				case 10:
					perfTestCommand10();
					break;
				case 50:
					perfTestCommand50();
					break;
				case 100:
					perfTestCommand100();
					break;
				default:
					perfTestCommand500();
				break;
				}
			}
			destroy();
		}
		
		public void perfTestCommand10(){
			if(verbose){
				System.out.println("In perfTestCommand10()");
			}
			try {
				TextMessage message = session.createTextMessage();
				message.setText(messageText);
				Destination replyToDestination = session.createTemporaryQueue();
				long start = System.currentTimeMillis();
				message.setJMSReplyTo(replyToDestination);
				sender.send(message);
				session.commit();
				MessageConsumer receiver = session.createConsumer(message
						.getJMSReplyTo(), "JMSCorrelationID = '"
						+ message.getJMSMessageID() + "'");
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				// 同期的にレシーブします。
				Message replyMessage = receiver.receive(5000);
				if (replyMessage != null) {
					if (replyMessage instanceof TextMessage) {
						if(verbose){
							System.out.println("Reply Message->"
								+ ((TextMessage) replyMessage).getText());
						}
					}
				} else {
					if(verbose){
						System.out.println("No message");
					}
				}
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				long end = System.currentTimeMillis();
				System.out.println("Total time taken for 10 records = " + (end-start) + " milliseconds");
				System.out.println("--------------------------------------------------------------");
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		
		public void perfTestCommand50(){
			if(verbose){
				System.out.println("In perfTestCommand50()");
			}
			try {
				TextMessage message = session.createTextMessage();
				message.setText(messageText);
				Destination replyToDestination = session.createTemporaryQueue();
				long start = System.currentTimeMillis();
				message.setJMSReplyTo(replyToDestination);
				sender.send(message);
				session.commit();				
				MessageConsumer receiver = session.createConsumer(message
						.getJMSReplyTo(), "JMSCorrelationID = '"
						+ message.getJMSMessageID() + "'");
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				// 同期的にレシーブします。
				Message replyMessage = receiver.receive(5000);
				if (replyMessage != null) {
					if (replyMessage instanceof TextMessage) {
						if(verbose){
							System.out.println("Reply Message->"
								+ ((TextMessage) replyMessage).getText());
						}
					}
				} else {
					if(verbose){
						System.out.println("No message");
					}
				}
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				long end = System.currentTimeMillis();
				System.out.println("Total time taken for50 records = " + (end-start) + " milliseconds");
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		
		public void perfTestCommand100(){
			if(verbose){
				System.out.println("In perfTestCommand100()");
			}
			try {
				TextMessage message = session.createTextMessage();
				message.setText(messageText);
				Destination replyToDestination = session.createTemporaryQueue();
				long start = System.currentTimeMillis();
				message.setJMSReplyTo(replyToDestination);
				sender.send(message);
				session.commit();				
				MessageConsumer receiver = session.createConsumer(message
						.getJMSReplyTo(), "JMSCorrelationID = '"
						+ message.getJMSMessageID() + "'");
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				// 同期的にレシーブします。
				Message replyMessage = receiver.receive(5000);
				if (replyMessage != null) {
					if (replyMessage instanceof TextMessage) {
						if(verbose){
							System.out.println("Reply Message->"
								+ ((TextMessage) replyMessage).getText());
						}
					}
				} else {
					System.out.println("No message");
				}
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				long end = System.currentTimeMillis();
				System.out.println("Total time taken for 100 records = " + (end-start) + " milliseconds");
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		
		public void perfTestCommand500(){
			if(verbose){
				System.out.println("In perfTestCommand500()");
			}
			try {
				TextMessage message = session.createTextMessage();
				message.setText(messageText);
				Destination replyToDestination = session.createTemporaryQueue();
				long start = System.currentTimeMillis();
				message.setJMSReplyTo(replyToDestination);
				sender.send(message);
				session.commit();				
				MessageConsumer receiver = session.createConsumer(message
						.getJMSReplyTo(), "JMSCorrelationID = '"
						+ message.getJMSMessageID() + "'");
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				// 同期的にレシーブします。
				Message replyMessage = receiver.receive(5000);
				if (replyMessage != null) {
					if (replyMessage instanceof TextMessage) {
						if(verbose){
							System.out.println("Reply Message->"
								+ ((TextMessage) replyMessage).getText());
						}
					}
				} else {
					if(verbose){
						System.out.println("No message");
					}
				}
				if(verbose){
					System.out.println("--------------------------------------------------------------");
				}
				long end = System.currentTimeMillis();
				System.out.println("Total time taken for 500 records = " + (end-start) + " milliseconds");
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		
		public void destroy(){
			if(verbose){
				System.out.println("Closing connection to JMS server..");
			}
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}
}