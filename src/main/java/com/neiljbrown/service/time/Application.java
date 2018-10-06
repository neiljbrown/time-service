/*
 *  Copyright 2018-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.neiljbrown.service.time;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Provides the entry point for running the application from the command line.
 * <p>
 * Annotated as an {@code @SpringBootApplication} class which enables the following behaviour:
 * <br>
 * - {@code @Configuration} - Acts as source of Spring managed beans using Spring Java config (@Bean annotated methods).
 * <br>
 * - {@code @ComponentScan} - Declares this class' package as the root for Spring component scanning.
 * <br>
 * - {@code @EnableAutoConfiguration} - Enables Spring Boot's intelligent convention-over-configuration behaviour for
 * guessing and configuring the Spring beans the app is likely to need, based on its environment, classpath etc.
 */
@SpringBootApplication
public class Application {

  // Prevent instantiation of class while it is a utility class containing only static methods, to keep checkstyle happy
  protected Application() {
  }

  /**
   * Supports running the application from the command line, e.g. when it is packaged as an executable JAR.
   * 
   * @param args array of command line arguments.
   */
  public static void main(String[] args) {
    // Create and return an instance of an appropriate class of Spring ApplicationContext (based on classpath), use
    // this class as the source of Spring beans, and expose any supplied command line args as Spring properties
    SpringApplication.run(Application.class, args);
  }
}