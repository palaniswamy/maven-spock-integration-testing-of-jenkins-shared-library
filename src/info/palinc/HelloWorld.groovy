package info.palinc

class HelloWorld implements Serializable {

    def script

    HelloWorld(def script) {
        this.script = script
    }

    def runMethod() {
        this.script.echo "Hello World!"
        return "success"
    }
}