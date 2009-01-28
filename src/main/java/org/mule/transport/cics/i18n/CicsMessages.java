package org.mule.transport.cics.i18n;

import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

public class CicsMessages extends MessageFactory {
	private static final String BUNDLE_PATH = getBundlePath("cics");
	
	public Message errorInConfigurationFile() {
		return createMessage(BUNDLE_PATH, 1);
	}
	
	public Message errorLoadingInterfaceFile(String interfaceFile){
		return createMessage(BUNDLE_PATH, 2, interfaceFile);
	}
	
	public Message errorParsingInterfaceFile(String operationName) {
		return createMessage(BUNDLE_PATH, 3, operationName);
	}

	public Message invalidEncodingForTransformer(String transformer,
			String encoding) {
		return createMessage(BUNDLE_PATH, 4, transformer, encoding);
	}
	
	public Message copyBookToXmlError(String xsdElement) {
		return createMessage(BUNDLE_PATH, 5, xsdElement);
	}
	
	public Message insufficientResponseLength() {
		return createMessage(BUNDLE_PATH, 6);
	}
	
	public Message errorInResposnse(String faultCode) {
		return createMessage(BUNDLE_PATH, 7, faultCode);
	}
	
	public Message operationNotSupported(String operation, String interfaceFile) {
		return createMessage(BUNDLE_PATH, 8, operation, interfaceFile);
	}
	
	public Message nullOperation() {
		return createMessage(BUNDLE_PATH, 9);
	}
	
	public Message errorOpeningFile(String fileName) {
		return createMessage(BUNDLE_PATH, 10, fileName);
	}

	public Message errorReadingFile(String fileName) {
		return createMessage(BUNDLE_PATH, 11, fileName);
	}
	
	public Message invalidLengthOfXsdElement(String length,
			String xsdElement) {
		return createMessage(BUNDLE_PATH, 12, length, xsdElement);
	}
	
	public Message errorReadingXsdFile(String xsdFile) {
		return createMessage(BUNDLE_PATH, 13, xsdFile);
	}
	
	public Message errorLoadingXsdFile(String xsdFile, String cause) {
		return createMessage(BUNDLE_PATH, 14, xsdFile, cause);
	}
	
	public Message errorReadingXsdEleFromResponse(String xsdElement) {
		return createMessage(BUNDLE_PATH, 15, xsdElement);
	}
	
	public Message unableToGetMsgSettings() {
		return createMessage(BUNDLE_PATH, 16);
	}
	
	public Message cicsConnectionError() {
		return createMessage(BUNDLE_PATH, 17);
	}
	
	public Message cicsAbendError(String msg) {
		return createMessage(BUNDLE_PATH, 18, msg);
	}
	
	public Message cicsSecurityError(String msg) {
		return createMessage(BUNDLE_PATH, 19, msg);
	}
	
	public Message cicsCommunicationError(String msg) {
		return createMessage(BUNDLE_PATH, 20, msg);
	}
	
	public Message cicsCTGError(String msg) {
		return createMessage(BUNDLE_PATH, 21, msg);
	}
	
	public Message cicsEISError(String msg) {
		return createMessage(BUNDLE_PATH, 22, msg);
	}	
}
