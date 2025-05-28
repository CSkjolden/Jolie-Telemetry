from console import Console
from string_utils import StringUtils
from ....main import Telemetry as telemetry
from ....main import PropagationData
from runtime import Runtime

type printArgument {
  content : undefined
  propagationData : PropagationData
}

interface PrettyConsoleAPI {
  requestResponse: 
    prettyPrint( printArgument )( void ),	
    prettyPrintln( printArgument )( void )
}

service PrettyConsole {
  execution: concurrent

  embed Console as console
  embed StringUtils as stringUtils
  embed telemetry as Telemetry
  embed Runtime as runtime

  inputPort ip {
    location: "local"
    aggregates: console
    interfaces: PrettyConsoleAPI
  }

  init
  {
    setTracer@Telemetry("PrettyConsole")()
  }

  main {
    [ prettyPrintln( request )() {
      scope (mainScope) {
        startProcess@Telemetry({propagationData << request.propagationData})( identifier )
        startSpan@Telemetry({id = identifier, spanName = "PrettyPrintln"})( )
        log@Telemetry({message = "PrettyPrintln", id = identifier, severity = "error", attributes << valueToPrettyString@stringUtils( request.content )})( )
        addAttributes@Telemetry({id = identifier, attributes << valueToPrettyString@stringUtils( request.content )})( )
        println@console( valueToPrettyString@stringUtils( request.content ) )()
        endSpan@Telemetry(identifier)( )
      }
    } ]
    {
      endProcess@Telemetry(identifier)( )
    }
    [ prettyPrint( request )() {
      scope (mainScope) {
        print@console( valueToPrettyString@stringUtils( request ) )()
      }
    } ]
  }
}