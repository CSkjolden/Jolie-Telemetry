package org.jolie_lang.joliex.telemetry.spec.faults;

public class ExistingProcessTelemetryException extends jolie.runtime.FaultException {
    
    private final java.lang.String fault;
    public ExistingProcessTelemetryException( java.lang.String fault ) {
        super( "ExistingProcessTelemetryException", jolie.runtime.Value.create( fault ) );
        this.fault = java.util.Objects.requireNonNull( fault );
    }
    
    public java.lang.String fault() { return fault; }
}