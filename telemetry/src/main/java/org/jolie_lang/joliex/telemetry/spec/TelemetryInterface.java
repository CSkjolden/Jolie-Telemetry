package org.jolie_lang.joliex.telemetry.spec;

public interface TelemetryInterface {
    
    @jolie.runtime.embedding.RequestResponse
    void addEvent( org.jolie_lang.joliex.telemetry.spec.types.AddEventParameters request ) throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException, org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    java.lang.String startProcess( org.jolie_lang.joliex.telemetry.spec.types.StartProcessParameters request ) throws org.jolie_lang.joliex.telemetry.spec.faults.ExistingProcessTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void log( org.jolie_lang.joliex.telemetry.spec.types.LogParameters request ) throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException, org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void endSpan( java.lang.String request ) throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException, org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void addAttributes( org.jolie_lang.joliex.telemetry.spec.types.AddAttributeParameters request ) throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException, org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void endProcess( java.lang.String request ) throws org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void startSpan( org.jolie_lang.joliex.telemetry.spec.types.CreateSpanParameters request ) throws org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void setTracer( java.lang.String request );
    
    java.lang.String getPropagationData( java.lang.String request ) throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException, org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void setStatus( org.jolie_lang.joliex.telemetry.spec.types.SetStatusParameters request ) throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException, org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException;
    
    @jolie.runtime.embedding.RequestResponse
    void shutdown();
}