type PropagationData : string

type CallInstanceIdentifier : string

type StartProcessParameters {
    id? : CallInstanceIdentifier
    propagationData? : PropagationData
}

type CreateSpanParameters {
    id : CallInstanceIdentifier
    spanName : string
}

type SetStatusParameters {
    id : CallInstanceIdentifier
    status : string( enum(["OK", "ERROR", "ok", "error"]) )
}

type AddAttributeParameters {
    id : CallInstanceIdentifier
    attributes : undefined 
}

type AddEventParameters {
    id : CallInstanceIdentifier
    eventName : string
    attributes? : undefined 
}

type LogParameters {
    message : string
    severity? : string( enum(["TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "trace", "debug", "info", "warn", "error", "fatal"]) )
    attributes? : undefined
    id? : CallInstanceIdentifier
}

interface TelemetryInterface {
    RequestResponse:
        setTracer( string )(void),
        startProcess(StartProcessParameters)(CallInstanceIdentifier) throws ExistingProcessTelemetryException(string),
        startSpan(CreateSpanParameters)(void) throws ProcessNotFoundTelemetryException(string),
        getPropagationData(CallInstanceIdentifier)(PropagationData) throws ProcessNotFoundTelemetryException(string) SpanNotFoundTelemetryException(string),
        addAttributes(AddAttributeParameters)(void) throws ProcessNotFoundTelemetryException(string) SpanNotFoundTelemetryException(string),
        addEvent(AddEventParameters)(void) throws ProcessNotFoundTelemetryException(string) SpanNotFoundTelemetryException(string),
        log(LogParameters)(void) throws ProcessNotFoundTelemetryException(string) SpanNotFoundTelemetryException(string),
        setStatus(SetStatusParameters)(void) throws ProcessNotFoundTelemetryException(string) SpanNotFoundTelemetryException(string),
        endSpan(CallInstanceIdentifier)(void) throws ProcessNotFoundTelemetryException(string) SpanNotFoundTelemetryException(string),
        endProcess(CallInstanceIdentifier)(void) throws ProcessNotFoundTelemetryException(string),
        shutdown(void)(void),
}

service Telemetry {
    inputPort ip {
        location: "local"
        interfaces: TelemetryInterface
    }
	foreign java {
		class: "org.jolie_lang.joliex.telemetry.Telemetry"
	}
}

