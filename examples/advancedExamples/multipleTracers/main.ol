// jolie -Dtelemetry.config=../examples/config.json --service main ../examples/advancedExamples/multipleTracers/main.ol

from ....telemetry.main import Telemetry as Telemetry
from ....telemetry.main import PropagationData

type multiplicationRequest {
  a : int
  b : int
  propagationData : PropagationData
}

type powerRequest {
  a : int
  b : int
  propagationData : PropagationData
}

interface MathServiceAPI {
  requestResponse: 
    multiplication( multiplicationRequest )( int ),
    power( powerRequest )( int )
}

service MathService {
  execution: concurrent

  embed Telemetry as simpleTelemetry
  embed Telemetry as advancedTelemetry

  inputPort ip {
    location: "local"
    interfaces: MathServiceAPI
  }

  init
  {
    setTracer@simpleTelemetry("SimpleMathService")() // Sets the tracer name for the simple telemetry service
    setTracer@advancedTelemetry("AdvancedMathService")() // Sets the tracer name for the advanced telemetry service
  }

  main {
    [ multiplication( request )( result ) {
      scope (mainScope) {
        startProcess@simpleTelemetry({propagationData << request.propagationData})( identifier )
        startSpan@simpleTelemetry({id = identifier, spanName = "Multiplication"})( )
        log@simpleTelemetry({message = "Multiplication", id = identifier, severity = "info", attributes << {a = request.a b = request.b}})( )
        result = request.a * request.b
        endSpan@simpleTelemetry(identifier)( )
      }
    } ]{ endProcess@simpleTelemetry(identifier)( )
      }
    [ power( request )( result ) {
      scope (mainScope) {
        startProcess@advancedTelemetry({propagationData << request.propagationData})( identifier )
        startSpan@advancedTelemetry({id = identifier, spanName = "Power"})( )
        log@advancedTelemetry({message = "Power", id = identifier, severity = "info", attributes << {a = request.a b = request.b}})( )
        multiplier = request.a
        while (request.b > 1) {
          request.a = request.a * multiplier
          request.b = request.b - 1
        }
        result = request.a
        endSpan@advancedTelemetry(identifier)( )}
    } ] { endProcess@advancedTelemetry(identifier)( )
      }
  }
}
service main( ) {
  execution: single
  embed Telemetry as telemetry
  embed MathService as mathService

  init
  {
    setTracer@telemetry("MultipleTracersExample")() // Sets the tracer name for the telemetry service
  }

  main{
    startProcess@telemetry()( identifier ) // Starts a new process and returns an identifier for it
    startSpan@telemetry({id = identifier, spanName = "Main Process"})( ) // Starts the first span of the process
    getPropagationData@telemetry(identifier)(propData)  // Retrieves the propagation data for the current process

    //Example of using MathService with propagation data
    {{multiplication@mathService({
      a = 5
      b = 10
      propagationData << propData
    })(resultMultiplication)}
    |
    {power@mathService({
      a = 2
      b = 3
      propagationData << propData
    })(resultPower)}}

    log@telemetry({message = "Results", id = identifier, severity = "info", attributes << {multiplicationResult = resultMultiplication, powerResult = resultPower}})( )

    endSpan@telemetry(identifier)( ) // Ends the first span of the process
    endProcess@telemetry(identifier)( ) // Ends the process and ends the span
    shutdown@telemetry()() // Useful to shutdown and cleanup the memory for a single execution service
  }
}