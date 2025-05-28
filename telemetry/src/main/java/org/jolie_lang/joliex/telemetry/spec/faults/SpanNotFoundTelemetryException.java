package org.jolie_lang.joliex.telemetry.spec.faults;

public class SpanNotFoundTelemetryException extends jolie.runtime.FaultException {
    
    private final java.lang.String fault;
    public SpanNotFoundTelemetryException( java.lang.String fault ) {
        super( "SpanNotFoundTelemetryException", jolie.runtime.Value.create( fault ) );
        this.fault = java.util.Objects.requireNonNull( fault );
    }
    
    public java.lang.String fault() { return fault; }
}