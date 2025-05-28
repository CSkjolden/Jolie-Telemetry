package org.jolie_lang.joliex.telemetry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.logs.export.SimpleLogRecordProcessor;

public class TelemetryConfig {
  private String serviceName;
  private String defaultTracerName = "StandardTracer";
  private List<SpanProcessorConfig> spanProcessors = new ArrayList<>();
  private SamplerConfig sampler = new AlwaysOnSamplerConfig();
  private CleanupConfig cleanupConfig = new CleanupConfig();
  private List<LogProcessorConfig> logProcessors = new ArrayList<>();

  public List<LogProcessorConfig> getLogProcessors() {
    return logProcessors;
  }

  public void setLogProcessors(List<LogProcessorConfig> loggingConfig) {
    this.logProcessors = loggingConfig;
  }

  public CleanupConfig getCleanupConfig() {
    return cleanupConfig;
  }

  public void setCleanupConfig(CleanupConfig cleanupConfig) {
    this.cleanupConfig = cleanupConfig;
  }

  public SamplerConfig getSampler() {
    return sampler;
  }

  public void setSampler(SamplerConfig sampler) {
    this.sampler = sampler;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getDefaultTracerName() {
    return defaultTracerName;
  }

  public void setDefaultTracerName(String instrumentationName) {
    this.defaultTracerName = instrumentationName;
  }

  public List<SpanProcessorConfig> getSpanProcessors() {
    return spanProcessors;
  }

  public void setSpanProcessors(List<SpanProcessorConfig> spanProcessors) {
    this.spanProcessors = spanProcessors;
  }
}

class CleanupConfig {
  private int timeoutInSeconds = 30;
  private boolean addEvent = true;
  private String eventMessage = "Jolie instance shutdown. Cleaning up spans.";
  private boolean setError = true;
  private boolean endSpan = true;

  public boolean isAddEvent() {
    return addEvent;
  }

  public void setAddEvent(boolean addEvent) {
    this.addEvent = addEvent;
  }

  public String getEventMessage() {
    return eventMessage;
  }

  public void setEventMessage(String eventMessage) {
    this.eventMessage = eventMessage;
  }

  public boolean isSetError() {
    return setError;
  }

  public void setSetError(boolean setError) {
    this.setError = setError;
  }

  public boolean isEndSpan() {
    return endSpan;
  }

  public void setEndSpan(boolean endSpan) {
    this.endSpan = endSpan;
  }

  public int getTimeoutInSeconds() {
    return timeoutInSeconds;
  }

  public void setTimeoutInSeconds(int timeoutInSeconds) {
    this.timeoutInSeconds = timeoutInSeconds;
  }

  public Consumer<io.opentelemetry.api.trace.Span> getSpanEnder() {
    Consumer<io.opentelemetry.api.trace.Span> consumer = span -> {
    };
    if (addEvent) {
      final String message = eventMessage;
      consumer = consumer.andThen(span -> span.addEvent(message));
    }
    if (setError) {
      consumer = consumer.andThen(span -> span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR));
    }
    if (endSpan) {
      consumer = consumer.andThen(span -> span.end());
    }
    return consumer;
  }

}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "processorType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BatchSpanProcessorConfig.class, name = "batch"),
    @JsonSubTypes.Type(value = SimpleSpanProcessorConfig.class, name = "simple"),
    @JsonSubTypes.Type(value = DebugSpanProcessorConfig.class, name = "debug")
})

abstract class SpanProcessorConfig {
  private ExporterConfig exporter;

  public ExporterConfig getExporter() {
    return exporter;
  }

  public void setExporter(ExporterConfig exporter) {
    this.exporter = exporter;
  }

  public abstract SpanProcessor createProcessor();
}

class DebugSpanProcessorConfig extends SpanProcessorConfig {
  @Override
  public SpanProcessor createProcessor() {
    return SimpleSpanProcessor.create(new SpanExporter() {
      @Override
      public CompletableResultCode export(Collection<SpanData> spans) {
        for (SpanData span : spans) {
          System.out.println(span);
        }
        return CompletableResultCode.ofSuccess();
      }

      @Override
      public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
      }

      @Override
      public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
      }
    });
  }

}

class BatchSpanProcessorConfig extends SpanProcessorConfig {
  private int maxExportBatchSize = 512;
  private int maxQueueSize = 2048;
  private int scheduleDelayInMs = 5000;

  public int getMaxExportBatchSize() {
    return maxExportBatchSize;
  }

  public void setMaxExportBatchSize(int maxExportBatchSize) {
    this.maxExportBatchSize = maxExportBatchSize;
  }

  public int getMaxQueueSize() {
    return maxQueueSize;
  }

  public void setMaxQueueSize(int maxQueueSize) {
    this.maxQueueSize = maxQueueSize;
  }

  public int getScheduleDelayInMs() {
    return scheduleDelayInMs;
  }

  public void setScheduleDelayInMs(int scheduleDelayInMs) {
    this.scheduleDelayInMs = scheduleDelayInMs;
  }

  @Override
  public SpanProcessor createProcessor() {
    return BatchSpanProcessor.builder(getExporter().createExporter())
        .setMaxExportBatchSize(maxExportBatchSize)
        .setMaxQueueSize(maxQueueSize)
        .setScheduleDelay(Duration.ofMillis(scheduleDelayInMs))
        .build();
  }
}

class SimpleSpanProcessorConfig extends SpanProcessorConfig {
  @Override
  public SpanProcessor createProcessor() {
    return SimpleSpanProcessor.create(getExporter().createExporter());
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "exporterType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpExporterConfig.class, name = "http"),
    @JsonSubTypes.Type(value = GrpcExporterConfig.class, name = "grpc")
})
abstract class ExporterConfig {
  private String endpoint;
  private int timeoutInSeconds = 30;
  private Map<String, String> headers;
  private int connectTimeoutInSeconds = 30;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public int getTimeoutInSeconds() {
    return timeoutInSeconds;
  }

  public void setTimeoutInSeconds(int timeoutInSeconds) {
    this.timeoutInSeconds = timeoutInSeconds;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public int getConnectTimeoutInSeconds() {
    return connectTimeoutInSeconds;
  }

  public void setConnectTimeoutInSeconds(int connectTimeoutInSeconds) {
    this.connectTimeoutInSeconds = connectTimeoutInSeconds;
  }

  public abstract SpanExporter createExporter();
}

class HttpExporterConfig extends ExporterConfig {
  @Override
  public SpanExporter createExporter() {
    var exporterBuilder = OtlpHttpSpanExporter.builder()
        .setEndpoint(getEndpoint())
        .setTimeout(Duration.ofSeconds(getTimeoutInSeconds()))
        .setConnectTimeout(Duration.ofSeconds(getConnectTimeoutInSeconds()));

    if (getHeaders() != null) {
      for (var entry : getHeaders().entrySet()) {
        exporterBuilder.addHeader(entry.getKey(), entry.getValue());
      }
    }

    return exporterBuilder.build();
  }
}

class GrpcExporterConfig extends ExporterConfig {
  @Override
  public SpanExporter createExporter() {
    var exporterBuilder = OtlpGrpcSpanExporter.builder()
        .setEndpoint(getEndpoint())
        .setTimeout(Duration.ofSeconds(getTimeoutInSeconds()));

    if (getHeaders() != null) {
      for (var entry : getHeaders().entrySet()) {
        exporterBuilder.addHeader(entry.getKey(), entry.getValue());
      }
    }

    return exporterBuilder.build();
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "samplerType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AlwaysOnSamplerConfig.class, name = "AlwaysOn"),
    @JsonSubTypes.Type(value = AlwaysOffSamplerConfig.class, name = "AlwaysOff"),
    @JsonSubTypes.Type(value = TraceIdRatioBasedSamplerConfig.class, name = "TraceIdRatioBased")
})

abstract class SamplerConfig {
  public abstract Sampler createSampler();
}

class AlwaysOnSamplerConfig extends SamplerConfig {
  @Override
  public Sampler createSampler() {
    return Sampler.alwaysOn();
  }
}

class AlwaysOffSamplerConfig extends SamplerConfig {
  @Override
  public Sampler createSampler() {
    return Sampler.alwaysOff();
  }
}

class TraceIdRatioBasedSamplerConfig extends SamplerConfig {
  private double ratio = 0.5;

  public double getRatio() {
    return ratio;
  }

  public void setRatio(double ratio) {
    this.ratio = ratio;
  }

  @Override
  public Sampler createSampler() {
    return Sampler.traceIdRatioBased(ratio);
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "processorType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BatchLogProcessorConfig.class, name = "batch"),
    @JsonSubTypes.Type(value = SimpleLogProcessorConfig.class, name = "simple"),
    @JsonSubTypes.Type(value = DebugLogProcessorConfig.class, name = "debug")
})
abstract class LogProcessorConfig {
  private LogExporterConfig exporter;

  public LogExporterConfig getExporter() {
    return exporter;
  }

  public void setExporter(LogExporterConfig exporter) {
    this.exporter = exporter;
  }

  public abstract LogRecordProcessor createProcessor();
}

class DebugLogProcessorConfig extends LogProcessorConfig {
  @Override
  public LogRecordProcessor createProcessor() {
    return SimpleLogRecordProcessor.create(new LogRecordExporter() {
      @Override
      public CompletableResultCode export(Collection<io.opentelemetry.sdk.logs.data.LogRecordData> logs) {
        for (var log : logs) {
          System.out.println("DEBUG LOG PROCESSOR: " + log);
        }
        return CompletableResultCode.ofSuccess();
      }

      @Override
      public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
      }

      @Override
      public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
      }
    });
  }
}

class BatchLogProcessorConfig extends LogProcessorConfig {
  private int maxExportBatchSize = 512;
  private int maxQueueSize = 2048;
  private int scheduleDelayInMs = 5000;

  public int getMaxExportBatchSize() {
    return maxExportBatchSize;
  }

  public void setMaxExportBatchSize(int maxExportBatchSize) {
    this.maxExportBatchSize = maxExportBatchSize;
  }

  public int getMaxQueueSize() {
    return maxQueueSize;
  }

  public void setMaxQueueSize(int maxQueueSize) {
    this.maxQueueSize = maxQueueSize;
  }

  public int getScheduleDelayInMs() {
    return scheduleDelayInMs;
  }

  public void setScheduleDelayInMs(int scheduleDelayInMs) {
    this.scheduleDelayInMs = scheduleDelayInMs;
  }

  @Override
  public LogRecordProcessor createProcessor() {
    return BatchLogRecordProcessor.builder(getExporter().createExporter())
        .setMaxExportBatchSize(maxExportBatchSize)
        .setMaxQueueSize(maxQueueSize)
        .setScheduleDelay(Duration.ofMillis(scheduleDelayInMs))
        .build();
  }
}

class SimpleLogProcessorConfig extends LogProcessorConfig {
  @Override
  public LogRecordProcessor createProcessor() {
    return SimpleLogRecordProcessor.create(getExporter().createExporter());
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "exporterType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = OtlpHttpLogExporterConfig.class, name = "http"),
    @JsonSubTypes.Type(value = OtlpGrpcLogExporterConfig.class, name = "grpc")
})
abstract class LogExporterConfig {
  private String endpoint;
  private int timeoutInSeconds = 30;
  private Map<String, String> headers;
  private int connectTimeoutInSeconds = 30;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public int getTimeoutInSeconds() {
    return timeoutInSeconds;
  }

  public void setTimeoutInSeconds(int timeoutInSeconds) {
    this.timeoutInSeconds = timeoutInSeconds;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public int getConnectTimeoutInSeconds() {
    return connectTimeoutInSeconds;
  }

  public void setConnectTimeoutInSeconds(int connectTimeoutInSeconds) {
    this.connectTimeoutInSeconds = connectTimeoutInSeconds;
  }

  public abstract LogRecordExporter createExporter();
}

class OtlpHttpLogExporterConfig extends LogExporterConfig {
  @Override
  public LogRecordExporter createExporter() {
    var exporterBuilder = OtlpHttpLogRecordExporter.builder()
        .setEndpoint(getEndpoint())
        .setConnectTimeout(Duration.ofSeconds(getConnectTimeoutInSeconds()))
        .setTimeout(Duration.ofSeconds(getTimeoutInSeconds()));

    if (getHeaders() != null) {
      for (var entry : getHeaders().entrySet()) {
        exporterBuilder.addHeader(entry.getKey(), entry.getValue());
      }
    }

    return exporterBuilder.build();
  }
}

class OtlpGrpcLogExporterConfig extends LogExporterConfig {
  @Override
  public LogRecordExporter createExporter() {
    var exporterBuilder = OtlpGrpcLogRecordExporter.builder()
        .setEndpoint(getEndpoint())
        .setConnectTimeout(Duration.ofSeconds(getConnectTimeoutInSeconds()))
        .setTimeout(Duration.ofSeconds(getTimeoutInSeconds()));

    if (getHeaders() != null) {
      for (var entry : getHeaders().entrySet()) {
        exporterBuilder.addHeader(entry.getKey(), entry.getValue());
      }
    }

    return exporterBuilder.build();
  }
}