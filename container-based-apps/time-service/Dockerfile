# **********************************************************************************************************************
# Coded instructions for building a Docker image capable of running this service in a Docker container. Also includes
# some help and advice on how to run the image.
# **********************************************************************************************************************

# Help > Running this Docker Image
# ================================
# To run the Docker image built from this Dockerfile use a docker run command similar to the following -
#
# $ docker run --detach -p 8080:8080 -e SPRING_PROFILES_ACTIVE="{profiles}" \
#    [-e JAVA_OPTS="-Dmy.system.property=foo ..."] \
#    --name time-service {dockerImageName}[:{tag}]
# where -
# + {profiles} - Is a common separate listed of one of more the service's Spring framework profiles to be activated.
# Typically this a single profile corresponding to the name of environment is used, e.g. int, test, stage or prod.
# + {dockerImageName} - Is the name of the service's Docker image specified when built, e.g. "neiljbrown/spring-faas-time-service".
# + {tag} - Optional image tag. If not specified then Docker defaults this to a tag of 'latest'.
#
# Support for Environment Specific App Config
# -------------------------------------------
# When relying on the static CMD instruction below to start-up the app, any dynamic environment specific application
# properties (such as 'spring.profiles.active') have to be set using environment variables rather than command line
# parameters. This can be achieved using the docker run command's -e option as shown above.

# ======================================================================================================================

# Use Docker Inc.'s official image for the Java Runtime Environment (JRE) for this service's base image.
# This base image uses Debian Linux for the O/S, and the OpenJDK distribution of the Java JRE. (See versions below).
# The Git repo for the base image can be found at https://github.com/docker-library/java.
# For more details see the base image's full readme at https://github.com/docker-library/docs/tree/master/openjdk
## OpenJDK implementation of Java Runtime Environment (JRE) for Java 8 update X, _slim_ variant, on Debian Linux 9.0.1 GA
FROM openjdk:8u162-jre-slim

ENV SERVICE_INSTALL_DIR /opt/time-service/
ENV SERVICE_JAR_FILE_NAME time-service.jar

# Add the applicaiton's released artefacts (in current directory) into container’s filesystem -
# Specified source files / folders must be below Docker's 'context' dir as specified by the docker build command
ADD $SERVICE_JAR_FILE_NAME $SERVICE_INSTALL_DIR

# Define the container ports to expose to the host - This is not mandatory for being able to map and access ports at
# runtime using the docker run -p/-publish flag) but it serves as documentation, and also provides a default set of
# ports to map when the docker run -P/--publish-all flag is used.
## The port that the app's Servlet/web container listens on for HTTP requests.
EXPOSE 8080
## The port that the JVM's JMX server might be configured (via JVM system prop) to listen on to support remote profiling
EXPOSE 12301

# Set the working directory for any subsequent RUN, CMD, etc instructions
WORKDIR $SERVICE_INSTALL_DIR

# Default command to execute on running container
# ===============================================
# Launches the service from its packaged executable fat jar, using the 'java -jar' command from command line -
# Uses shell form of CMD instruction rather than exec (JSON array) format to support expansion of environment vars.
#
# The following comments explain the purpose of the java options used in the CMD instruction below, where not obvious -
#
# Non-standard & Experimental VM Options
# --------------------------------------
# -XshowSettings:vm - Output includes the JVM's estimated max heap size which can be used to confirm it is respecting
# any RAM limits specified for the container.
#
# -XX:+UseCGroupMemoryLimitForHeap - Configure the OpenJDK 8 JVM to respect the RAM limits of the container in which
# it is running if one is specified when calculating its max available heap size, rather assuming all of host's RAM is
# available. (This is only an experimental feature in JDK 8, having been back-ported from JDK 9). Note that JDK 8+
# already respects any specified container limits for max CPU cores. For more details see
# https://github.com/docker-library/docs/tree/master/openjdk#make-jvm-respect-cpu-and-ram-limits
#
# System Properties
# -----------------
# java.security.egd - Avoids delaying (blocking) application startup on the creation of SecureRandom when there is lack
# of entropy by reconfiguring the source of entropy from the default of /dev/random to /dev/urandom. The downsides of
# switching to /dev/urandom need to be considered by apps with strong cryptographic requirements, which is not the case
# for this service. For more details see Jira ticket AC-412.
#
# Applying Additional, Arbitrary Java Options
# -------------------------------------------
# Any additional Java options can be supplied (dynamically) via the 'JAVA_OPTS' environment variable. This can be used
# for example to override Java (heap and/or metaspace) memory spaces, or enable remote profiling, without needing to
# change the Dockerfile, rebuild the image, and have to re-release.
#
# Java Resource & Performance Tuning Options
# ------------------------------------------
# Java options for setting resource usage such as Heap and Metaspace memory space sizing are no longer hard-coded in
# this file. If the JVM's default settings for such resource usage do need to be overridden then (as noted above) for
# flexibility they should be specified via the 'JAVA_OPTS' env variable. (These settings should be informed by (soak
# and load) testing).
#
# Adding support for Remote Profiling of JVM Using Metrics Exposed by JMX server
# ------------------------------------------------------------------------------
# To enable profiling of the JVM running in the container using a remote JMX console (e.g. VisualVM) set the following
# additional Java system properties using the JAVA_OPTS environment variable -
#   -Dcom.sun.management.jmxremote.port=<jmx-remote-port>
#   -Dcom.sun.management.jmxremote.rmi.port=<jmx-remote-port>
#   -Djava.rmi.server.hostname=<rmi-server-hostname-or-ip>
#   -Dcom.sun.management.jmxremote.authenticate=false # Disables security, not recommended for production usage!!
#   -Dcom.sun.management.jmxremote.ssl=false
# Where <rmi-server-hostname-or-ip> is the hostname or IP address of the Docker host. (In dev environments that use
# Docker Machine (previously boot2docker) this will be the IP address of the virtual machine in which Docker runs).
# And <jmx-remote-port> is a free port on the Docker host, e.g. 12301.
#
# Then extend the docker run command to inform the Docker host that the container listens on <jmx-remote-port>, and
# publish / map the RMI port to the port on the host server, e.g.
# $ docker run --expose <jmx-remote-port> -p 8080:8080 -p <jmx-remote-port>:<jmx-remote-port> \
#     -e JAVA_OPTS="-Dcom.sun.management.jmxremote.port=..." ...
#
# A similar equivalent command for running the service natively (outside of Docker) with env specific properties is:
# $ java -server -Duser.timezone=UTC -Djava.security.egd=file:/dev/./urandom -jar \
#     /opt/time-service/time-service.jar --spring.profiles.active=test --server.port=8080 &
CMD java -server \
  -showversion \
  -XshowSettings:vm \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap \
  -Duser.timezone=UTC \
  -Djava.security.egd=file:/dev/./urandom \
  $JAVA_OPTS \
  -jar $SERVICE_JAR_FILE_NAME