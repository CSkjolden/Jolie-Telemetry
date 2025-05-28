package org.jolie_lang.joliex.telemetry.spec.faults;

public class ProcessNotFoundTelemetryException extends jolie.runtime.FaultException {
    
    private final java.lang.String fault;
    public ProcessNotFoundTelemetryException( java.lang.String fault ) {
        super( "ProcessNotFoundTelemetryException", jolie.runtime.Value.create( fault ) );
        this.fault = java.util.Objects.requireNonNull( fault );
    }
    
    public java.lang.String fault() { return fault; }
}