import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;

public class JibxBindingProvider {

  public String javaObjectToXml(Object object) throws Exception {
	IBindingFactory requestFactory = BindingDirectory.getFactory(object.getClass());
	// marshal object to String (with nice indentation, as UTF-8)
	IMarshallingContext mctx = requestFactory.createMarshallingContext();
	mctx.setIndent(2);
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	mctx.marshalDocument(object, "UTF-8", null, bos);
	return new String(bos.toByteArray(), "UTF-8");
  }

  public Object xmlToJavaObject(String xml, Class responseClass) throws Exception {
	IBindingFactory responseFactory = BindingDirectory.getFactory(responseClass);
	IUnmarshallingContext uctx = responseFactory.createUnmarshallingContext();
	ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	return uctx.unmarshalDocument(bis, null);
  }

}
