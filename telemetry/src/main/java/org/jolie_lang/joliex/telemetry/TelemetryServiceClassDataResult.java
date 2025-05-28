package org.jolie_lang.joliex.telemetry;

import java.util.concurrent.ConcurrentHashMap;

public class TelemetryServiceClassDataResult {
  private final ConcurrentHashMap<String, ProcessData> telemetryServiceClassData;
  private final String telemetryServiceClassId;

  public TelemetryServiceClassDataResult(ConcurrentHashMap<String, ProcessData> processData, String id) {
    this.telemetryServiceClassData = processData;
    this.telemetryServiceClassId = id;
  }

  public ConcurrentHashMap<String, ProcessData> getTelemetryServiceClassData() {
    return telemetryServiceClassData;
  }

  public String getTelemetryServiceClassId() {
    return telemetryServiceClassId;
  }
}