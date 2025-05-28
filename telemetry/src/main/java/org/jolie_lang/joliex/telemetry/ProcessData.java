package org.jolie_lang.joliex.telemetry;

import java.util.ArrayDeque;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;

public class ProcessData {
  private final ArrayDeque<Span> spans;
  private final Context context;

  public ProcessData(Context context) {
    this.spans = new ArrayDeque<>();
    this.context = context;
  }

  public ArrayDeque<Span> getSpans() {
    return spans;
  }

  public Context getContext() {
    return context;
  }
}