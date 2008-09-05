import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

public class CastorBindingProvider {

    public String javaObjectToXml(Object object) throws Exception {
    
        java.io.Writer writer = new java.io.StringWriter();
        Marshaller.marshal(object, writer);
        return writer.toString();    
    }

    public Object xmlToJavaObject(String xml, Class responseClass) throws Exception {
    
        java.io.Reader reader = new java.io.StringReader(xml);
        return Unmarshaller.unmarshal(responseClass, reader);    
    }

}
