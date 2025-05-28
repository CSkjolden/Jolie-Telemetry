// jolie -Dtelemetry.config=../examples/config.json --service main ../examples/simpleExamples/propagationData/main.ol
from ....telemetry.main import Telemetry as Telemetry
from ....telemetry.main import PropagationData

interface continueServiceAPI {
  requestResponse: 
    operation( PropagationData )( void )
}

service main( ) {
	execution: single
	embed Telemetry as telemetry
  embed continueService as continueService

  init
  {
    setTracer@telemetry("PropagationExample")() // Sets the tracer name for the telemetry service
  }

  main{
    startProcess@telemetry()( identifier ) // Starts a new process and returns an identifier for it
    startSpan@telemetry({id = identifier, spanName = "Process start"})( ) // Starts the first span of the process
    operation@continueService(getPropagationData@telemetry(identifier))() // Calls the continueService with the propagation data
    endSpan@telemetry(identifier)( ) // Ends the first span of the process
    endProcess@telemetry(identifier)(  ) // Ends the process and ends the span
    shutdown@telemetry()() // Useful to shutdown and cleanup the memory for a single execution service
  }
}

service continueService {
  execution: sequential
  embed Telemetry as telemetry

  inputPort ip {
    location: "local"
    interfaces: continueServiceAPI
  }

  init
  {
    setTracer@telemetry("ContinueService")() // Sets the tracer name for the telemetry service
  }

  main{
    [ operation( propData )() {
      scope (mainScope) {
        startProcess@telemetry({propagationData << propData})( identifier ) // Starts a new process with the propagation data
        startSpan@telemetry({id = identifier, spanName = "ContinueService operation"})( ) // Starts the first span of the process while continuing the propagation data
        endSpan@telemetry(identifier)( )
      }
    } ]
    {
      endProcess@telemetry(identifier)( )
    }

  }
}


