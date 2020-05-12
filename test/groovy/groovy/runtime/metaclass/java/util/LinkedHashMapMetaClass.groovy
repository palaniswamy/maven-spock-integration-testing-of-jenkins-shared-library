package groovy.runtime.metaclass.java.util

import info.palinc.JenkinsStep

class LinkedHashMapMetaClass extends DelegatingMetaClass {

    LinkedHashMapMetaClass(MetaClass delegate) {
        super(delegate)
    }

    LinkedHashMapMetaClass(Class theClass) {
        super(theClass)
    }

    @Override
    @SuppressWarnings('cast')
    Object invokeMethod(Object instance, String methodName, Object[] arguments) {
        try {
            return super.invokeMethod(instance, methodName, arguments)
        } catch (MissingMethodException e) {
            LinkedHashMap map = instance as LinkedHashMap

            if (map.containsKey(methodName) && map.get(methodName) instanceof JenkinsStep) {
                JenkinsStep step = (JenkinsStep)map.get(methodName)

                arguments.each { Object obj ->
                    if (obj instanceof Closure) {
                        (obj as Closure).call()
                    }
                }
                if (arguments.size() > 0 && arguments[0] instanceof Map) {
                    if (arguments.size() > 1) {
                        return step.call(arguments[0] as Map, arguments.drop(1))
                    }
                    return step.call(arguments[0] as Map)
                }
                return step.call(arguments)
            }
            return super.invokeMethod(instance, methodName, arguments)
        }
    }

}
