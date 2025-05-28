package org.jolie_lang.joliex.telemetry;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;

public class TelemetryCore {
  private TelemetryConfig config;
  private Tracer tracer;
  private OpenTelemetrySdk sdk;
  private final Map<String, ConcurrentHashMap<String, ProcessData>> telemetryServiceDataMap = new ConcurrentHashMap<>();

  public Tracer getTracer() {
    return tracer;
  }

  public Tracer getTracer(String instrumentationName) {
    return sdk.getTracer(instrumentationName);
  }

  public Logger getLogger() {
    return sdk.getSdkLoggerProvider().get(config.getServiceName());
  }

  public TelemetryServiceClassDataResult getTelemetryServiceDataMap() {
    var id = UUID.randomUUID().toString();
    var map = new ConcurrentHashMap<String, ProcessData>();
    telemetryServiceDataMap.put(id, map);
    return new TelemetryServiceClassDataResult(map, id);
  }

  public void removeTelemetryServiceDataFromMap(String id) {
    if (telemetryServiceDataMap.containsKey(id)) {
      var dataMap = telemetryServiceDataMap.get(id);
      processSpans(dataMap, config.getCleanupConfig().getSpanEnder());
      telemetryServiceDataMap.remove(id);
    }
  }

  public TelemetryCore() {
    var fileName = System.getProperty("telemetry.config", "./telemetry.config");

    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("Telemetry configuration file path is not set or empty.");
    }

    try {
      this.config = loadConfiguration(new File(fileName));
    } catch (IOException e) {
      throw new RuntimeException("Failed to load telemetry configuration", e);
    }
    initOpenTelemetry();
  }

  private TelemetryConfig loadConfiguration(File configFile) throws IOException {
    if (!configFile.exists() || !configFile.isFile()) {
      throw new IOException("Configuration file does not exist or is not a file: " + configFile.getAbsolutePath());
    }
    return getConfigFromFile(configFile);
  }

  private static TelemetryConfig getConfigFromFile(File configFile) throws IOException {
    var mapper = new ObjectMapper();
    return mapper.readValue(configFile, TelemetryConfig.class);
  }

  private void initOpenTelemetry() {
    var loggerProvider = createLoggerProvider();
    var tracerProvider = createTracerProvider();

    sdk = io.opentelemetry.sdk.OpenTelemetrySdk.builder()
        .setTracerProvider(tracerProvider)
        .setLoggerProvider(loggerProvider)
        .buildAndRegisterGlobal();
    tracer = sdk.getTracer(config.getDefaultTracerName());
    addCleanupDuringShutdown(tracerProvider, loggerProvider);
  }

  private SdkLoggerProvider createLoggerProvider() {
    var loggerProviderBuilder = SdkLoggerProvider.builder()
        .setResource(createResource());

    for (var processorConfig : config.getLogProcessors()) {
      loggerProviderBuilder.addLogRecordProcessor(processorConfig.createProcessor());
    }

    return loggerProviderBuilder.build();
  }

  private SdkTracerProvider createTracerProvider() {
    var tracerProviderBuilder = SdkTracerProvider.builder()
        .setResource(createResource());

    for (var processorConfig : config.getSpanProcessors()) {
      tracerProviderBuilder.addSpanProcessor(processorConfig.createProcessor());
    }

    tracerProviderBuilder.setSampler(config.getSampler().createSampler());

    return tracerProviderBuilder.build();
  }

  private Resource createResource() {
    return Resource.getDefault().toBuilder()
        .put("service.name", config.getServiceName())
        .build();
  }

  private void addCleanupDuringShutdown(SdkTracerProvider tracerProvider, SdkLoggerProvider loggerProvider) {
    var cleanupConfig = config.getCleanupConfig();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      var spanConsumer = cleanupConfig.getSpanEnder();

      telemetryServiceDataMap.values().parallelStream()
          .forEach(nodeMap -> processSpans(nodeMap, spanConsumer));

      tracerProvider.forceFlush().join(cleanupConfig.getTimeoutInSeconds(), TimeUnit.SECONDS);
      loggerProvider.forceFlush().join(cleanupConfig.getTimeoutInSeconds(), TimeUnit.SECONDS);

      sdk.shutdown().join(cleanupConfig.getTimeoutInSeconds(), TimeUnit.SECONDS);

    }));
  }

  private void processSpans(ConcurrentHashMap<String, ProcessData> processDataMap,
      java.util.function.Consumer<Span> spanConsumer) {
    processDataMap.values().parallelStream()
        .map(processData -> processData.getSpans())
        .flatMap(spans -> spans.stream())
        .forEach(spanConsumer::accept);
  }
}