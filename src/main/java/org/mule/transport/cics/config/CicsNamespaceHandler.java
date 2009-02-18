package org.mule.transport.cics.config;

import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;

import org.mule.config.spring.factories.OutboundEndpointFactoryBean;
import org.mule.config.spring.parsers.specific.ServiceDefinitionParser;
import org.mule.config.spring.parsers.specific.TransformerDefinitionParser;
import org.mule.config.spring.parsers.specific.endpoint.TransportEndpointDefinitionParser;
import org.mule.config.spring.parsers.specific.endpoint.TransportGlobalEndpointDefinitionParser;

import org.mule.endpoint.URIBuilder;

import org.mule.transport.cics.transformers.*;
import org.mule.transport.cics.CicsService;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

//public class CicsNamespaceHandler extends NamespaceHandlerSupport {
/**
 * Describe class <code>CicsNamespaceHandler</code> here.
 */
public class CicsNamespaceHandler extends AbstractMuleNamespaceHandler {
	/**
     * Describe <code>init</code> method here.
     *
     */
    public void init() {

        String protocol = "cics";
        registerStandardTransportEndpoints(protocol, new String[]{protocol}).addAlias(protocol, URIBuilder.PATH);


		registerBeanDefinitionParser("service", new ServiceDefinitionParser(CicsService.class));

		// register the parser for endpoints with cics protocol.

		String[] requiredAttributes = {};
		registerBeanDefinitionParser("cics-endpoint",new TransportGlobalEndpointDefinitionParser(protocol , requiredAttributes));
		registerBeanDefinitionParser("cics-outbound-endpoint", new TransportEndpointDefinitionParser(protocol , OutboundEndpointFactoryBean.class , requiredAttributes));

		// register the parser for endpoints with stubtest protocol.
		protocol = "stubtest";
		registerBeanDefinitionParser("dummy-response-endpoint", new TransportGlobalEndpointDefinitionParser(protocol , requiredAttributes));

        // register the transformers
		registerBeanDefinitionParser("log-transformer" , new TransformerDefinitionParser(LoggingTransformer.class));
		registerBeanDefinitionParser("copybook-to-xml" , new TransformerDefinitionParser(CopyBookToXml.class));
		registerBeanDefinitionParser("xml-to-copybook" , new TransformerDefinitionParser(XmlToCopyBook.class));
		registerBeanDefinitionParser("soap-to-xml" , new TransformerDefinitionParser(SoapToXml.class));
		registerBeanDefinitionParser("xml-to-soap" , new TransformerDefinitionParser(XmlToSoap.class));
		registerBeanDefinitionParser("rest-to-xml" , new TransformerDefinitionParser(RestToXml.class));
		registerBeanDefinitionParser("xml-to-rest" , new TransformerDefinitionParser(XmlToRest.class));
		registerBeanDefinitionParser("xml-inbound-transformer" , new TransformerDefinitionParser(XmlInboundTransformer.class));
		registerBeanDefinitionParser("exception-to-fault-message" , new TransformerDefinitionParser(ExceptionToFaultMessage.class));
		registerBeanDefinitionParser("bytearray-to-xml" , new TransformerDefinitionParser(ByteArrayToXml.class));
		registerBeanDefinitionParser("ogis-copybook-incoming-handler" , new TransformerDefinitionParser(OGISCopyBookIncomingHandler.class));
		registerBeanDefinitionParser("ogis-copybook-outgoing-handler" , new TransformerDefinitionParser(OGISCopyBookOutgoingHandler.class));
		registerBeanDefinitionParser("ogis-copybook-to-xml" , new TransformerDefinitionParser(OGISCopyBookToXml2.class));
	}
}
