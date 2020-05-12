package info.palinc

import spock.lang.Specification

class HelloWorldSpec extends Specification {

    def "runMethod() returns success"() {
        given:
        def script = [
                echo: Mock(JenkinsStep)
        ]
        def helloWorld = new HelloWorld(script)

        when:
        def result = helloWorld.runMethod()

        then:
        1 * script.echo.call("Hello World")
        result == 'success'
    }

}
