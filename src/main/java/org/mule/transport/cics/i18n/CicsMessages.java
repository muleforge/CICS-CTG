package org.mule.transport.cics.i18n;

import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

public class CicsMessages extends MessageFactory {
	private static final String BUNDLE_PATH = getBundlePath("cics");
	
	public static Message errorInConfigurationFile() {
		return createMessage(BUNDLE_PATH, 1);
	}
	
	public static Message errorLoadingInterfaceFile(String interfaceFile){
		return createMessage(BUNDLE_PATH, 2, interfaceFile);
	}
	
	public static Message errorParsingInterfaceFile(String operationName) {
		return createMessage(BUNDLE_PATH, 3, operationName);
	}

	public static Message invalidEncodingForTransformer(String transformer,
			String encoding) {
		return createMessage(BUNDLE_PATH, 4, transformer, encoding);
	}
	
	public static Message copyBookToXmlError(String xsdElement) {
		return createMessage(BUNDLE_PATH, 5, xsdElement);
	}
	
	public static Message insufficientResponseLength() {
		return createMessage(BUNDLE_PATH, 6);
	}
	
	public static Message errorInResposnse(String faultCode) {
		return createMessage(BUNDLE_PATH, 7, faultCode);
	}
	
	public static Message operationNotSupported(String operation, String interfaceFile) {
		return createMessage(BUNDLE_PATH, 8, operation, interfaceFile);
	}
	
	public static Message nullOperation() {
		return createMessage(BUNDLE_PATH, 9);
	}
	
	public static Message errorOpeningFile(String fileName) {
		return createMessage(BUNDLE_PATH, 10, fileName);
	}

	public static Message errorReadingFile(String fileName) {
		return createMessage(BUNDLE_PATH, 11, fileName);
	}
	
	public static Message invalidLengthOfXsdElement(String length,
			String xsdElement) {
		return createMessage(BUNDLE_PATH, 12, length, xsdElement);
	}
	
	public static Message errorReadingXsdFile(String xsdFile) {
		return createMessage(BUNDLE_PATH, 13, xsdFile);
	}
	
	public static Message errorLoadingXsdFile(String xsdFile, String cause) {
		return createMessage(BUNDLE_PATH, 14, xsdFile, cause);
	}
	
	public static Message errorReadingXsdEleFromResponse(String xsdElement) {
		return createMessage(BUNDLE_PATH, 15, xsdElement);
	}
	
	public static Message unableToGetMsgSettings() {
		return createMessage(BUNDLE_PATH, 16);
	}
	
	public static Message cicsConnectionError() {
		return createMessage(BUNDLE_PATH, 17);
	}
	
	public static Message cicsAbendError(String msg) {
		return createMessage(BUNDLE_PATH, 18, msg);
	}
	
	public static Message cicsSecurityError(String msg) {
		return createMessage(BUNDLE_PATH, 19, msg);
	}
	
	public static Message cicsCommunicationError(String msg) {
		return createMessage(BUNDLE_PATH, 20, msg);
	}
	
	public static Message cicsCTGError(String msg) {
		return createMessage(BUNDLE_PATH, 21, msg);
	}
	
	public static Message cicsEISError(String msg) {
		return createMessage(BUNDLE_PATH, 22, msg);
	}	
}
