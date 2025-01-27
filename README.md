# Show project for spring native bug - spring-data-mongodb gives cglib error for interface referenced beans

When `org.springframework:spring-tx` is imported, java native projects with
repository beans defined with interface types will encounter exception in runtime about unexpected
CGLIB usage for the repository class.

## Steps for reproduction

1. Compile `./mvnw clean package -Pnative -DskipTests`
2. Run `./target/demo`

- Usually, when a bean is defined with interface type, cglib proxy is not necessary
  - process-aot does not generate `MongoDao$$SpringCGLIB$$0.class` class in
    target/spring-aot/main/classes.
- however, in runtime, something is trying to wrap `MongoDao` with CGLIB proxy in runtime, resulting
  in below exception.

```stacktrace
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'demoApplication': Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'myDao': Unexpected AOP exception
        at org.springframework.beans.factory.aot.BeanInstanceSupplier.resolveAutowiredArgument(BeanInstanceSupplier.java:369)
        at org.springframework.beans.factory.aot.BeanInstanceSupplier.resolveArguments(BeanInstanceSupplier.java:289)
        at org.springframework.beans.factory.aot.BeanInstanceSupplier.get(BeanInstanceSupplier.java:223)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.obtainInstanceFromSupplier(DefaultListableBeanFactory.java:979)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.obtainFromSupplier(AbstractAutowireCapableBeanFactory.java:1243)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1186)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:563)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523)
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:336)
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:307)
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:334)
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.instantiateSingleton(DefaultListableBeanFactory.java:1122)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingleton(DefaultListableBeanFactory.java:1093)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:1030)
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:987)
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:627)
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:752)
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:318)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1361)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1350)
        at com.example.demo.DemoApplication.main(DemoApplication.java:18)
        at java.base@21.0.2/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'myDao': Unexpected AOP exception
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:608)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523)
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:336)
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:307)
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:334)
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
        at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1631)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1519)
        at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:913)
        at org.springframework.beans.factory.support.RegisteredBean.resolveAutowiredArgument(RegisteredBean.java:253)
        at org.springframework.beans.factory.aot.BeanInstanceSupplier.resolveAutowiredArgument(BeanInstanceSupplier.java:366)
        ... 23 more
Caused by: org.springframework.aop.framework.AopConfigException: Unexpected AOP exception
        at org.springframework.aop.framework.CglibAopProxy.buildProxy(CglibAopProxy.java:243)
        at org.springframework.aop.framework.CglibAopProxy.getProxy(CglibAopProxy.java:167)
        at org.springframework.aop.framework.ProxyFactory.getProxy(ProxyFactory.java:110)
        at org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor.postProcessAfterInitialization(AbstractAdvisingBeanPostProcessor.java:127)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization(AbstractAutowireCapableBeanFactory.java:439)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1815)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:601)
        ... 34 more
Caused by: java.lang.UnsupportedOperationException: CGLIB runtime enhancement not supported on native image. Make sure to include a pre-generated class on the classpath instead: com.example.demo.MongoDao$$SpringCGLIB$$0
        at org.springframework.cglib.core.AbstractClassGenerator.generate(AbstractClassGenerator.java:363)
        at org.springframework.cglib.proxy.Enhancer.generate(Enhancer.java:575)
        at org.springframework.cglib.core.AbstractClassGenerator$ClassLoaderData.lambda$new$1(AbstractClassGenerator.java:107)
        at org.springframework.cglib.core.internal.LoadingCache.lambda$createEntry$1(LoadingCache.java:52)
        at java.base@21.0.2/java.util.concurrent.FutureTask.run(FutureTask.java:317)
        at org.springframework.cglib.core.internal.LoadingCache.createEntry(LoadingCache.java:57)
        at org.springframework.cglib.core.internal.LoadingCache.get(LoadingCache.java:34)
        at org.springframework.cglib.core.AbstractClassGenerator$ClassLoaderData.get(AbstractClassGenerator.java:130)
        at org.springframework.cglib.core.AbstractClassGenerator.create(AbstractClassGenerator.java:317)
        at org.springframework.cglib.proxy.Enhancer.createHelper(Enhancer.java:562)
        at org.springframework.cglib.proxy.Enhancer.createClass(Enhancer.java:407)
        at org.springframework.aop.framework.ObjenesisCglibAopProxy.createProxyClassAndInstance(ObjenesisCglibAopProxy.java:62)
        at org.springframework.aop.framework.CglibAopProxy.buildProxy(CglibAopProxy.java:228)
        ... 40 more
```

## Expected behaviour

Either

- stop referencing cglib classes in native runtime for my interface-defined repository bean

Or

- update spring-aot to pre-generate cglib classes for repository beans even they are defined with
  interface types

## Known workarounds

- Define all repository beans with concreate type
  at [BeanConfig](./src/main/java/com/example/demo/BeanConfig.java), see code comments.

## Used Versions

- JDK: `21.0.2-graalce`
- Spring-Boot: `3.4.2` (latest to date)
- MacOS `15.2` on ARM64 mac

## Special thanks

This issue is not exactly the same but shares many symptoms as

- https://github.com/spring-projects/spring-framework/issues/32609
- https://github.com/MartinLei/springNativeBugBeanComposition
