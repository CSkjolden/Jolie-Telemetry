from console import Console
from string-utils import StringUtils
from ..main import Telemetry as telemetry
from time import Time

service main( ) {

	embed Console as console
	embed StringUtils as stringUtils
	embed telemetry as telemetry
	embed Time as time
    
	execution: single

    main{
			scope (TestScope1) {
				install( ProcessNotFoundTelemetryException => println@console(valueToPrettyString@stringUtils(TestScope1.TelemetryFaultException))())

				endSpan@telemetry( "test" )() // Throws unknown process exception
			}
			
			scope (TestScope2) {
				install( SpanNotFoundTelemetryException => println@console(valueToPrettyString@stringUtils(TestScope2.TelemetryFaultException))())

				startProcess@telemetry()(TestIdentifier)
				endSpan@telemetry( TestIdentifier )() // Throws unknown process exception
			}

      startProcess@telemetry()( identifier )
			startSpan@telemetry({id = identifier, spanName = "Process start"})( )
			startSpan@telemetry({id = identifier, spanName = "Test"})( )
			getPropagationData@telemetry(identifier)( propData )

			// prebuiltList = "TestValue"
			prebuiltList[1] = "Hello"
			prebuiltList[2] = "World"

			testNumber = 25
			testString = "Hello World"
			testBoolean = true
			testList[1] = "Hello"
			testList[2] = "World"
			testList[3] = 25
			testList[4] = true
			testList[5] = 3.14
			testList[6] << {
				testNumber = 25
			}
			testList[7] << prebuiltList

			// sameTypeList[0] = 0
			sameTypeList[1] = 1
			sameTypeList[2] = 2
			sameTypeList[3] = 3
			sameTypeList[4] << prebuiltList
			config << {
				testNumber = testNumber
				testString = testString
				testBoolean = testBoolean
				testList << testList
				sameTypeList << sameTypeList
			}
			config = "TestValue"
			println@console(valueToPrettyString@stringUtils(config))()

			attributes = "Ignored value"
			bunchOfValues[0] = 16
			bunchOfValues[1] = "Some information"
			bunchOfValues[2] = 3.14159
			bunchOfNumbers[0] = 2
			bunchOfNumbers[1] = 4
			bunchOfNumbers[2] = 8
			bunchOfNumbers[3] << bunchOfValues // Everything but the first element will be ignored
			attributes << {
				ImportantNumber = 25
				ImportantString = "Very important information"
				CakeIsALie = true
				ListOfValues << bunchOfValues
				ListOfNumbers << bunchOfNumbers
			}

			addEvent@telemetry({id = identifier, eventName = "TestEventForValues", attributes << attributes})( )

			addAttributes@telemetry({id = identifier, attributes << config})( )
			addEvent@telemetry({id = identifier, eventName = "TestEvent", attributes << config})( )
			println@console(valueToPrettyString@stringUtils(propData))()
			setStatus@telemetry({id = identifier, status = "OK"})( )
			endSpan@telemetry(identifier)( )
			setStatus@telemetry({id = identifier, status = "ERROR"})( )
			endProcess@telemetry(identifier)(  )
			shutdown@telemetry()()
    }
}