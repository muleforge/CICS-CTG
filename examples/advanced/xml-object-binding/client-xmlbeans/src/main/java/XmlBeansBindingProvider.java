import org.apache.xmlbeans.XmlObject;

public class XmlBeansBindingProvider {

    public String javaObjectToXml(Object object) throws Exception {
	    XmlObject xmlObject = (XmlObject) object;
	    return xmlObject.xmlText();
    }

    public Object xmlToJavaObject(String xml, Class responseClass) throws Exception {
	    return XmlObject.Factory.parse(xml);
    }

}
