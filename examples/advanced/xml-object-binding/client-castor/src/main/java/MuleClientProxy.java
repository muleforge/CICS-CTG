import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextFactory;
import org.mule.config.builders.SimpleConfigurationBuilder;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.module.client.MuleClient;

public class MuleClientProxy {

    protected static transient Log logger = LogFactory.getLog(MuleClientProxy.class);

    private static boolean isContextStarted = false;
    private MuleClient muleClient = null;

    public MuleClientProxy(String configFile) throws Exception {
      if (isContextStarted == false) {
          MuleContext context = createMuleContext(configFile);
          context.start();
          isContextStarted = true;
      }

	  muleClient = new MuleClient();
    }

    private MuleContext createMuleContext(String configFile) throws Exception {
      List builders = new ArrayList();
      builders.add(new SimpleConfigurationBuilder(null));
      builders.add(new SpringXmlConfigurationBuilder(configFile));
      MuleContextFactory ctxFactory = new DefaultMuleContextFactory();
      return ctxFactory.createMuleContext(builders, new DefaultMuleContextBuilder());
    }

    public String requestSend(String url, String requestXML) throws Exception {

	    logger.info("Sending to URL " + url);
	    logger.info("requestXML = " + requestXML);
	    MuleMessage result = muleClient.send(url, requestXML, null);
	    String responseXML = result.getPayloadAsString();
	    logger.info("responseXML = " + responseXML);
	    return responseXML;
    }
}
