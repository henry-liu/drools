package org.drools.core.marshalling.impl;

import java.io.IOException;
import java.util.List;

import org.kie.runtime.process.ProcessInstance;

public interface ProcessMarshaller {

    void writeProcessInstances( MarshallerWriteContext context ) throws IOException;

    void writeProcessTimers( MarshallerWriteContext context ) throws IOException;

    void writeWorkItems( MarshallerWriteContext context ) throws IOException;

    List<ProcessInstance> readProcessInstances( MarshallerReaderContext context ) throws IOException;

    void readProcessTimers( MarshallerReaderContext context ) throws IOException, ClassNotFoundException;

    void readWorkItems( MarshallerReaderContext context ) throws IOException;

    void init(MarshallerReaderContext context);

}
