# Show project for spring native bug - composition of beans through interfaces
Configuration classes with bean composition over interfaces are not recognized by the processAot task.
Therefore, the generated class by the processAot "BeanConfig__BeanDefinitions.java" does not contain the defined bean information from interface BeanA.java

## Steps for reproduction
1. Compile ./gradlew nativeCompile
2. Run ./build/native/nativeCompile/demo
- The task processAot does not generate necessary Bean Definitions from BeanConfig.java
````
org.springframework.beans.factory.UnsatisfiedDependencyException: 
Error creating bean with name 'demoApplication': Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'createACar': Instantiation of supplied bean failed
````
- If the Bean had been defined in a class like BeanConfig.java instead by an interface the processAot would generate
the necessary Bean Definition to run it native.

## Wanted outcome
Bean composition over interface for Configuration classes for native images would be nice.
Example BeanConfig.java


## Used Versions
- JDK: Oracle GraalVM 22
- Spring-Boot: 3.2.4

