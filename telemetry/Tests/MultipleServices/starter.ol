from console import Console
from string_utils import StringUtils
from ...main import Telemetry as telemetry

from .TestFolder.prettyConsole import PrettyConsole
from time import Time
from file import File
from runtime import Runtime

service starter {
  execution: single

  embed Console as console
  embed PrettyConsole as prettyConsole
  embed StringUtils as stringUtils
  embed telemetry as Telemetry
  embed Time as time
  embed File as file
  embed Runtime as runtime

  init
  {
    setTracer@Telemetry("Starter")()
  }

  main {
    startProcess@Telemetry({id = "Test", propagationData = "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01"})(TestIdentifier)
    startSpan@Telemetry({id = TestIdentifier, spanName = "Starter class init"})( )
    log@Telemetry({message = "Test"})()
    endProcess@Telemetry(TestIdentifier)( )
    startProcess@Telemetry()( identifier )
    startSpan@Telemetry({id = identifier, spanName = "Starter class init"})( )
    startSpan@Telemetry({id = identifier, spanName = "Calling PrettyConsole"})( )
    addEvent@Telemetry({id = identifier, eventName = "PrettyConsole"})( )
    getPropagationData@Telemetry(identifier)(propData)
    {{prettyPrintln@prettyConsole( {content = "Test" propagationData << propData})()} |
    {prettyPrintln@prettyConsole( {content = "Test2" propagationData << propData})()}}
    setStatus@Telemetry({id = identifier, status = "OK"})( )
    endSpan@Telemetry(identifier)( )
    endProcess@Telemetry(identifier)( )





    
  }
}