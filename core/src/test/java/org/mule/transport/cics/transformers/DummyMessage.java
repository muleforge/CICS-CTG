package org.mule.transport.cics.transformers;

import org.mule.DefaultMuleMessage;
import org.mule.message.DefaultExceptionPayload;

public class DummyMessage extends DefaultMuleMessage {

    public DummyMessage(Object message, Exception e) {
      super(message, new java.util.HashMap());
      super.setExceptionPayload(new DefaultExceptionPayload(e));
    }

    public DummyMessage(Object message) {
      super(message, new java.util.HashMap());
    }
}
