package org.jolie_lang.joliex.telemetry;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;

public final class Telemetry extends jolie.runtime.JavaService
        implements org.jolie_lang.joliex.telemetry.spec.TelemetryInterface {

    private static final TelemetryCore telemetry = new TelemetryCore();
    private Tracer tracer = telemetry.getTracer();
    private ConcurrentHashMap<String, ProcessData> processMap;
    private String serviceClassId;
    private Logger log = telemetry.getLogger();

    public Telemetry() {
        var serviceData = telemetry.getTelemetryServiceDataMap();
        processMap = serviceData.getTelemetryServiceClassData();
        serviceClassId = serviceData.getTelemetryServiceClassId();
    }

    public void addEvent(org.jolie_lang.joliex.telemetry.spec.types.AddEventParameters request)
            throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException,
            org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processIdentifier = request.id();
        var eventValue = request.eventName();
        var rawAttributes = request.attributes().orElse(null);
        var processData = getProcessDataOrThrow(processIdentifier);

        synchronized (processData) {
            var span = getActiveSpanOrThrow(processData, processIdentifier);
            if (rawAttributes != null) {
                var attributes = extractAttributes(rawAttributes);
                span.addEvent(eventValue, attributes);
            } else {
                span.addEvent(eventValue);
            }
        }
    }

    public java.lang.String startProcess(org.jolie_lang.joliex.telemetry.spec.types.StartProcessParameters request)
            throws org.jolie_lang.joliex.telemetry.spec.faults.ExistingProcessTelemetryException {
        var processIdentifier = request.id().orElse(java.util.UUID.randomUUID().toString());
        var propagationData = request.propagationData().orElse(null);
        Context context;
        if (propagationData == null) {
            context = Context.root();
        } else {
            TextMapPropagator propagator = W3CTraceContextPropagator.getInstance();
            context = propagator.extract(
                    Context.root(),
                    propagationData,
                    new TextMapGetter<String>() {
                        @Override
                        public Iterable<String> keys(String carrier) {
                            return List.of("traceparent");
                        }

                        @Override
                        public String get(String carrier, String key) {
                            return carrier;
                        }
                    });
        }
        var processData = new ProcessData(context);
        if (processMap.containsKey(processIdentifier)) {
            throw createExistingProcessTelemetryException(processIdentifier);
        }
        processMap.put(processIdentifier, processData);

        return processIdentifier;
    }

    public void endSpan(java.lang.String processIdentifier)
            throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException,
            org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processData = getProcessDataOrThrow(processIdentifier);

        synchronized (processData) {
            getActiveSpanOrThrow(processData, processIdentifier).end();
        }
    }

    public void addAttributes(org.jolie_lang.joliex.telemetry.spec.types.AddAttributeParameters request)
            throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException,
            org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processIdentifier = request.id();
        var attributes = extractAttributes(request.attributes());
        var processData = getProcessDataOrThrow(processIdentifier);

        synchronized (processData) {
            getActiveSpanOrThrow(processData, processIdentifier).setAllAttributes(attributes);
        }
    }

    public void endProcess(java.lang.String processIdentifier)
            throws org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processData = processMap.remove(processIdentifier);
        if (processData == null) {
            throw createProcessNotFoundTelemetryException(processIdentifier);
        }

        processData.getSpans().forEach(span -> {
            span.end();
        });
    }

    public void startSpan(org.jolie_lang.joliex.telemetry.spec.types.CreateSpanParameters request)
            throws org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processIdentifier = request.id();
        var spanName = request.spanName();

        var processData = getProcessDataOrThrow(processIdentifier);

        synchronized (processData) {
            var newSpanBuilder = tracer.spanBuilder(spanName);

            if (!processData.getSpans().isEmpty()) {
                var lastSpan = processData.getSpans().peekFirst();
                newSpanBuilder.setParent(lastSpan.storeInContext(processData.getContext()));
            } else {
                newSpanBuilder.setParent(processData.getContext());
            }
            processData.getSpans().addFirst(newSpanBuilder.startSpan());
        }
    }

    public void log(org.jolie_lang.joliex.telemetry.spec.types.LogParameters request)
            throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException,
            org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processIdentifier = request.id().orElse(null);
        var message = request.message();
        var severity = request.severity().orElse("info");
        var attributes = request.attributes().orElse(null);

        var logBuilder = log.logRecordBuilder();

        if (attributes != null) {
            var logAttributes = extractAttributes(attributes);
            logBuilder.setAllAttributes(logAttributes);
        }

        if (processIdentifier != null) {
            var processData = getProcessDataOrThrow(processIdentifier);
            synchronized (processData) {
                var span = getActiveSpanOrThrow(processData, processIdentifier);
                logBuilder.setContext(span.storeInContext(processData.getContext()));
            }
        }

        switch (severity.toLowerCase()) {
            case "debug":
                logBuilder.setSeverity(Severity.DEBUG);
                break;
            case "error":
                logBuilder.setSeverity(Severity.ERROR);
                break;
            case "warn":
                logBuilder.setSeverity(Severity.WARN);
                break;
            case "info":
            default:
                logBuilder.setSeverity(Severity.INFO);
                break;
        }

        logBuilder.setBody(message);

        logBuilder.emit();
    }

    public void setTracer(java.lang.String instrumentationName) {
        tracer = telemetry.getTracer(instrumentationName);
    }

    public java.lang.String getPropagationData(java.lang.String request)
            throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException,
            org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processIdentifier = request;
        var processData = getProcessDataOrThrow(processIdentifier);

        synchronized (processData) {
            var span = getActiveSpanOrThrow(processData, processIdentifier);

            java.util.Map<String, String> carrier = new java.util.HashMap<>();
            W3CTraceContextPropagator.getInstance()
                    .inject(
                            span.storeInContext(processData.getContext()),
                            carrier,
                            java.util.Map::put);
            String traceparent = carrier.get("traceparent");
            return traceparent;
        }
    }

    public void setStatus(org.jolie_lang.joliex.telemetry.spec.types.SetStatusParameters request)
            throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException,
            org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        var processIdentifier = request.id();
        var status = request.status();
        var processData = getProcessDataOrThrow(processIdentifier);

        synchronized (processData) {
            var span = getActiveSpanOrThrow(processData, processIdentifier);
            io.opentelemetry.api.trace.StatusCode statusCode;
            switch (status.toLowerCase()) {
                case "ok":
                    statusCode = io.opentelemetry.api.trace.StatusCode.OK;
                    break;
                case "error":
                    statusCode = io.opentelemetry.api.trace.StatusCode.ERROR;
                    break;
                default:
                    statusCode = io.opentelemetry.api.trace.StatusCode.UNSET;
                    break;
            }
            span.setStatus(statusCode);
        }
    }

    public void shutdown() {
        telemetry.removeTelemetryServiceDataFromMap(serviceClassId);
    }

    private static Attributes extractAttributes(jolie.runtime.embedding.java.JolieValue attributes) {
        var attributeMap = attributes.children();
        var attributesBuilder = Attributes.builder();

        attributeMap.forEach((key, value) -> {
            if (value.size() == 1) {
                addSingleValue(attributesBuilder, key.toString(), value.get(0));
            } else {
                addArrayValue(attributesBuilder, key.toString(), value);
            }
        });

        return attributesBuilder.build();
    }

    private static void addSingleValue(AttributesBuilder builder, String key,
            jolie.runtime.embedding.java.JolieValue rawValue) {
        var tempValue = jolie.runtime.embedding.java.JolieValue.toValue(rawValue);
        if (tempValue.isBool()) {
            builder.put(key, tempValue.boolValue());
        } else if (tempValue.isInt()) {
            builder.put(key, tempValue.intValue());
        } else if (tempValue.isDouble()) {
            builder.put(key, tempValue.doubleValue());
        } else if (tempValue.isLong()) {
            builder.put(key, tempValue.longValue());
        } else if (tempValue.isString()) {
            builder.put(key, tempValue.strValue());
        }
    }

    private static void addArrayValue(AttributesBuilder builder, String key,
            List<jolie.runtime.embedding.java.JolieValue> value) {
        List<jolie.runtime.Value> valueStream = value.stream()
                .map(jolie.runtime.embedding.java.JolieValue::toValue)
                .filter(jolie.runtime.Value::isDefined)
                .toList();
        if (valueStream.isEmpty())
            return;
        AttributeType commonType = null;
        commonType = valueStream.stream()
                .map(v -> {
                    if (v.isString())
                        return AttributeType.String;
                    if (v.isInt())
                        return AttributeType.Int;
                    if (v.isDouble())
                        return AttributeType.Double;
                    if (v.isLong())
                        return AttributeType.Long;
                    if (v.isBool())
                        return AttributeType.Boolean;
                    return AttributeType.Mixed;
                })
                .distinct()
                .reduce((a, b) -> AttributeType.Mixed)
                .orElse(commonType);
        switch (commonType) {
            case String:
                builder.put(key, valueStream.stream().map(jolie.runtime.Value::strValue).toArray(String[]::new));
                break;
            case Int:
                builder.put(key, valueStream.stream().mapToDouble(jolie.runtime.Value::intValue).toArray());
                break;
            case Double:
                builder.put(key, valueStream.stream().mapToDouble(jolie.runtime.Value::doubleValue).toArray());
                break;
            case Long:
                builder.put(key, valueStream.stream().mapToLong(jolie.runtime.Value::longValue).toArray());
                break;
            case Boolean:
                var boolArray = new boolean[valueStream.size()];
                for (int i = 0; i < valueStream.size(); i++) {
                    boolArray[i] = valueStream.get(i).boolValue();
                }
                builder.put(key, boolArray);
                break;
            case Mixed:
            default:
                builder.put(key, valueStream.stream()
                        .map(v -> v.isString() ? v.strValue() : v.valueObject().toString())
                        .toArray(String[]::new));
                break;
        }
    }

    private ProcessData getProcessDataOrThrow(String processIdentifier)
            throws org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException {
        ProcessData processData = processMap.get(processIdentifier);
        if (processData == null) {
            throw createProcessNotFoundTelemetryException(processIdentifier);
        }
        return processData;
    }

    private Span getActiveSpanOrThrow(ProcessData processData, String processIdentifier)
            throws org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException {
        if (processData.getSpans().isEmpty()) {
            throw createSpanNotFoundTelemetryException(processIdentifier);
        }
        return processData.getSpans().peekFirst();
    }

    private org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException createProcessNotFoundTelemetryException(
            java.lang.String processName) {
        return new org.jolie_lang.joliex.telemetry.spec.faults.ProcessNotFoundTelemetryException(
                "Process not found: " + processName);
    }

    private org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException createSpanNotFoundTelemetryException(
            java.lang.String processName) {
        return new org.jolie_lang.joliex.telemetry.spec.faults.SpanNotFoundTelemetryException(
                "Span list is empty for process: " + processName);
    }

    private org.jolie_lang.joliex.telemetry.spec.faults.ExistingProcessTelemetryException createExistingProcessTelemetryException(
            java.lang.String processName) {
        return new org.jolie_lang.joliex.telemetry.spec.faults.ExistingProcessTelemetryException(
                "Process already exists: " + processName);
    }
}
