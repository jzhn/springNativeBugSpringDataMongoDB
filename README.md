# Show project for spring native bug - composition of beans through interfaces
Configuration classes with bean composition over interfaces are not recognized by the processAot task.
Therefore, the generated class BeanConfig__BeanDefinitions.java does not contain the defined bean from interface BeanA.java
Which would be the case if the bean from BeanA.java would be directly in BeanConfig.java

## Steps for reproduction
1. ./gradlew processAot
2. ./build/generated/aotSources/com.example.demo/config/BeanConfig__BeanDefinitions

