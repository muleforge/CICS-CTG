import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbBindingProvider {

    public String javaObjectToXml(Object object) throws Exception {
	    String pkgName = object.getClass().getPackage().getName();
	    Marshaller marshaller = JAXBContext.newInstance(pkgName)
	    .createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		    Boolean.TRUE);
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    marshaller.marshal(object, bos);
	    return new String(bos.toByteArray(), "UTF-8");
    }

    public Object xmlToJavaObject(String xml, Class responseClass) throws Exception {
	    String pkgName = responseClass.getPackage().getName();
	    Unmarshaller unmarshaller = JAXBContext.newInstance(pkgName)
	    .createUnmarshaller();
	    ByteArrayInputStream bis = new ByteArrayInputStream(xml
		    .getBytes("UTF-8"));
	    return unmarshaller.unmarshal(bis);
    }

}
