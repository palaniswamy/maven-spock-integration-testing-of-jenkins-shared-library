import info.palinc.HelloWorld

def call(){
    new HelloWorld(this).runMethod()
    // to print all inherited steps
    println(this.steps.functions.keySet())
    // to test if inherited steps are invocable.
    echo "Hello World!"
}