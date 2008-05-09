package org.mule.transport.cicsStreaming.transformers;

import java.io.ByteArrayOutputStream;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transport.OutputHandler;
import org.mule.api.transformer.TransformerException;
import org.mule.api.MuleMessage;

import org.mule.transport.cics.util.Constants;

/*
 * Used to convert a OutputHandler to String.
 */
public class OutputHandlerToString extends AbstractMessageAwareTransformer {

  public Object transform(MuleMessage message, String encoding) throws TransformerException {

      try {
          Object payload = (OutputHandler) message.getPayload();
          if (payload instanceof OutputHandler) {
              ByteArrayOutputStream bos = new ByteArrayOutputStream();
              ((OutputHandler) payload).write(null, bos);
              return new String(bos.toByteArray(), Constants.XML_DEFAULT_ENCODING);
          }

          return payload;
      } catch (Throwable e) {
          throw new TransformerException(this, e);
      }
  }
}
