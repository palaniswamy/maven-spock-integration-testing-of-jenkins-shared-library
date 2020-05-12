package info.palinc

interface JenkinsStep {

    def call(Map map)
    def call(Map map, Object... args)
    def call(Object... args)
    def call()

}