# Time service sample app

## Overview
This project contains a sample REST API service known as the 'Time service'. It's a simple, but realistic backend 
service that provides a couple of external REST APIs, built using Java and Spring Boot, and packaged & deployed as 
Docker container. 

### Application software stack
The service is built on a software stack of Java (8.x), Spring Boot 2.1.x / Spring (5.1.x) and Tomcat (9) web 
container (Servlet API 4.0).

### Dev Environment Setup / Prerequisites
To build, test, package, deploy and run this project locally you will need a dev env comprising the following  -    
  
*1) JDK* - Install the correct major version (see above) of the JDK used by this service to support compiling the 
source code and running the app. 
  
*2) Gradle* - Gradle is used to build the service. If you run any of the service build script's tasks via the 
gradle wrapper - ./gradlew - it will automatically download and install the required version of Gradle. See below for 
more details.  
  
*3) Docker* - The service is packaged and deployed as a Docker image. This requires a Docker (client and 
server/engine) to be installed in the dev ('local') environment. For non-Linux (i.e. Mac OS X, or Windows) dev 
environments, install Docker using the corresponding native app (e.g. Docker for Mac).

## Source Code
The project uses the standard Maven directory layout for a Java app. Java source code and resoures can be found in the 
src/main/java and src/main/resources folders respectively. Automated test code and resources can be found in  
the src/test/java and src/test/resources folders.  

## Building the service
The service is built, tested and released using Gradle. For a list of available build tasks enter the following 
command in the project root directory:

```./gradlew tasks```

Automated unit tests and fine-grained intra-service integration tests (that entail launching the Spring container) are 
implemented in JUnit (5) and AssertJ . To execute the unit tests from the command line, enter the following command:

```./gradlew test```

To assemble (compile and package) the service execute the following commands:

```./gradlew assemble```

This will create an executable, fat JAR in the build/libs folder.

To assemble the service, and additionally run the service's checks (tests and any coding standards) beforehand, use the 
'build' task, e.g.  

```./gradlew build``` 

For more details see build.gradle in the project root directory.

## Running the service 
You can run the service 'in-place' (without assembling it) from your workspace using the following command:

```./gradlew bootRun```

To terminate the application, press CTRL-C.

The service is assembled (compiled and packaged) into an executable JAR which includes its own embedded web container. 
It can therefore be run 'standalone' from any location. To assemble the service and run the application from its 
assembled JAR in a background process attached to your shell, enter the following commands:

```
./gradlew assemble
java -jar ./build/libs/time-service-{version}.jar [--spring.profiles.active=local] &
```

where {version} is the project version, e.g. 0.0.1-snapshot.

The 'spring.profiles.active' option is used to select the Spring bean profiles which should be activated. This defaults 
to 'local' - the base profile for the development environment. For more details see the comments in the service's 
application config file - src/main/resources/application.yaml.  
 
If the service starts up successfully you will see a pageful of info-level logs output to your screen, which will 
end with a line similar to the following:
<pre>
...
2019-01-21 21:35:36.304  INFO 90658 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-01-21 21:35:36.309  INFO 90658 --- [           main] com.neiljbrown.service.time.Application  : Started Application in 2.984 seconds (JVM running for 3.571)
</pre>

To terminate the background process from a bash shell, use the following command - 

```kill $(ps aux | grep '[t]ime-service' | awk '{print $2}')```

--
