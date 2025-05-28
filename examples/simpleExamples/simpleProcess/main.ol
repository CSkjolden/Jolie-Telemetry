//jolie -Dtelemetry.config=config.json simpleExamples/simpleProcess/main.ol
from jolie-telemetry import Telemetry

service main( ) {
	execution: single
	embed Telemetry as telemetry

  init
  {
    setTracer@telemetry("simpleSingleExecutionExample")() // Sets the tracer name for the telemetry service
  }

  main{
    startProcess@telemetry()( identifier ) // Starts a new process and returns an identifier for it
    startSpan@telemetry({id = identifier, spanName = "Process start"})( ) // Starts the first span of the process
    // Workload
    endSpan@telemetry(identifier)( ) // Ends the first span of the process
    endProcess@telemetry(identifier)(  ) // Ends the process and ends the span
    shutdown@telemetry()() // Useful to shutdown and cleanup the memory for a single execution service
  }
}