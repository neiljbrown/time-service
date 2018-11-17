/*
 * Copyright 2018-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neiljbrown.service.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A (narrow) set of Spring container integration tests for {@link Application}.
 * <p>
 * Provides a cursory test of the integration of the application with Spring / Spring Boot, including the application's
 * dependency injection config (wiring of Spring managed 'beans') for the selected current bean profile(s).
 * <p>
 * Implemented as a JUnit test case, which uses the Spring TestContext framework's support (e.g. for loading the
 * Spring ApplicationContext from configured beans before the first test runs, and reloading it if it becomes dirty
 * etc).
 */
@ExtendWith(SpringExtension.class) // Enable execution of Spring TestContext support in JUnit test case lifecycle
// Bootstrap app using Spring Boot support e.g. load application.properties, create default beans based on classpath etc
// Load a WebApplicationContext that uses a MockServletContext, rather than launching full web container
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.MOCK)
public class ApplicationSpringIntegrationTest {

  /**
   * Tests that the application's entire Spring bean configuration for the active bean profile(s) is valid (without the
	 * need to deploy the app) by attempting to load them into a Spring WebApplicationContext. Uses {@link Application}
	 * as the source of the application's Spring beans.
   */  
	@Test
	void contextLoads() {
	}
}