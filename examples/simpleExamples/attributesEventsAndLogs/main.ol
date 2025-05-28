//jolie -Dtelemetry.config=../examples/config.json ../examples/simpleExamples/attributesEventsAndLogs/main.ol
from ....telemetry.main import Telemetry as Telemetry

service main( ) {
	execution: single
	embed Telemetry as telemetry

  init
  {
    log@telemetry({message = "Telemetry service initialized" severity = "info"})() // Logs a message to the telemetry service
    setTracer@telemetry("AttributesEventAndLogExamples")() // Sets the tracer name for the telemetry service
  }

  main{
    // Declaring attributes
    attributes = "Ignored value" // Will not be included
    bunchOfValues[0] = 8
    bunchOfValues[1] = "Some information"
    bunchOfValues[2] = 3.14159
    bunchOfNumbers[0] = 2
    bunchOfNumbers[1] = 4
    bunchOfNumbers[2] << bunchOfValues // Everything but the first element will be ignored
    attributes << {
      ImportantNumber = 25
      ImportantString = "Very important information"
      CakeIsALie = true
      ListOfValues << bunchOfValues
      ListOfNumbers << bunchOfNumbers
    }

    startProcess@telemetry()( identifier ) // Starts a new process and returns an identifier for it
    startSpan@telemetry({id = identifier, spanName = "Process start"})( ) // Starts the first span of the process
    addAttributes@telemetry({id = identifier, attributes << attributes})( ) // Adds attributes to the current span
    addEvent@telemetry({id = identifier, eventName = "Event with attributes", attributes << attributes})( ) // Adds an event to the current span
    // Workload
    log@telemetry({message = "Log with both attributes and trace context", id = identifier, severity = "info", attributes << attributes})( ) // Logs a message to the telemetry service with attributes
    setStatus@telemetry({id = identifier, status = "OK"})( ) // Sets the status of the current span
    endSpan@telemetry(identifier)( ) // Ends the first span of the process
    endProcess@telemetry(identifier)(  ) // Ends the process and ends the span
    shutdown@telemetry()() // Useful to shutdown and cleanup the memory for a single execution service
  }
}