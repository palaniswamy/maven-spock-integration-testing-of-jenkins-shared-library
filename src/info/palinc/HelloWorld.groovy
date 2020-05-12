package info.palinc

class HelloWorld implements Serializable {

    def script

    HelloWorld(Object script) {
        this.script = script
    }

    def runMethod() {
        script.echo "Hello World"
        return "success"
    }
}