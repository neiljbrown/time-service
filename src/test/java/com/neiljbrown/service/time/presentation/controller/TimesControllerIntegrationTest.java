/*
 * Copyright 2018 - present the original author or authors.
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

package com.neiljbrown.service.time.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;
import com.neiljbrown.service.time.Application;
import com.neiljbrown.service.time.presentation.ApiErrorCode;

/**
 * A narrow set of integration tests of the {@link TimesController} REST API controller in conjunction with its
 * supporting web stack (Spring MVC framework), using a mocked Servlet web container, including serialisation of any
 * HTTP request and response bodies.
 * <p>
 * API Controller IntegrationTest classes such as this one contain a small no. of tests that focus on testing the
 * Controller is capable of processing and responding to API requests based on the correct use of web stack, in a
 * simpler and faster way than launching and making HTTP requests to the real web container.
 * <p>
 * These integration tests are _not_ intended as a substitute for unit tests, but rather supplement them with a fewer
 * no. of higher level tests.
 *
 * <h2>Test Implementation</h2>
 * In-process - Tests run in the same process (JVM for the JUnit test runner) as the code (Controller) under test.
 * <p>
 * Uses the Spring TestContext framework to load the application's Spring application context/container, and its
 * extension the Spring MVC Test framework, to load a mock Servlet container, in the JUnit JVM.
 * <p>
 * Tests exercise the Controller's public web endpoints as an API client would using HTTP request semantics (rather
 * than invoking its methods directly as in a unit test), but without making actual remote calls. This is done via a
 * (local) method call to the Spring MVC mock server API {@link MockMvc}.
 *
 * <h2>Included Test Coverage/Scope</h2>
 * Uses the Spring MVC Test framework to integration test the API Controller in conjunction with Spring MVC (including
 * DispatcherServlet and Spring MVC's request processing machinery), downwards. Provides the following test coverage:
 * <ul>
 * <li>Spring bean config for whole Spring container (although these are covered by other integration tests also);</li>
 * <li>&#64;Controller annotations (&#64;RestController, &#64;RequestMapping) for mapping requests to handler
 * methods</li>
 * <li>&#64;Controller annotations (&#64;RequestBody) for marshalling of HTTP request body and binding to request
 * params to method params (&#64;PathVariable, &#64;RequestParam);
 * </li>
 * <li>&#64;Controller annotations (&#64;ResponseStatus and &#64;ResponseBody) for setting the HTTP success response
 * status and marshalling of returned object to HTTP response body.</li>
 * </ul>
 *
 * <h2>Excluded Test Coverage/Scope</h2>
 * Scope is limited to testing the Controller. Any collaborating business logic invoked by the controller handler
 * method is mocked-out. (These tests are therefore more like unit tests from this perspective).
 * <p>
 * These tests use {@link MockMvc} to test against mock Servlet classes rather than the production Servlet/web
 * container, with a view to making the tests quick to run. Consequently, the tests are NOT a substitute for other
 * broader ('component' or 'system') tests when the app is fully-deployed and running in the production servlet
 * container (which may either be running in the same or separate, remote process to the tests) and invoked,
 * over-the-wire by a production API client.
 *
 * @see <a href=
 * "https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-framework">Spring
 * Reference Manual > Testing chapter > 3. Integration Testing > 3.6 Spring MVC Test Framework</a>
 */
@ExtendWith(SpringExtension.class) // Enable execution of Spring TestContext support in JUnit test case lifecycle
// Bootstrap app using Spring Boot support e.g. load application.properties, create default beans based on classpath etc
// Load a WebApplicationContext that uses a MockServletContext, rather than launching full web container
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TimesControllerIntegrationTest {

  private final WebApplicationContext wac;

  // Spring MVC Test framework's main interface for integration testing a web Controller on top of a mocked Servlet
  // container. Supports both making requests to the Controller, via Spring MVC, and asserting the response in terms
  // of a set of one or more expectations.
  private MockMvc mockMvc;

  /**
   * @param wac this application's Spring {@link WebApplicationContext}. Required by the Spring MVC Test
   * framework to support configuring the Spring MVC web stack infrastructure (DispatcherServlet etc) to use a mock
   * Servlet container/APIs.
   */
  public TimesControllerIntegrationTest(WebApplicationContext wac) {
    this.wac = wac;
  }

  @BeforeEach
  void setUp() {
    // Create new instance of class used to perform requests and set expectations on the Controller response
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  /**
   * Tests making a valid request to the 'Get Platform Time' API that includes all the optional request params, and
   * which results in a success response.
   * <p>
   * Serves to test the following integration points -
   * <br/>
   * - Mapping / routing of requests to the expected handler based on HTTP method, URL path, compatible Accept header.
   * <br/>
   * - Binding of mandatory and optional request parameters to Controller method arguments.
   * <br/>
   * - Return of the expected response for a RestController, including HTTP response status code; and HTTP response
   * body containing serialisation of the Controller's returned result in the requested media-type.
   *
   * @throws Exception if an unexpected error occurs on execution of this test.
   */
  @Test
  void getPlatformTimeWhenValidRequestWithOptionalRequestParams() throws Exception {
    final MediaType  acceptableMediaType = MediaType.APPLICATION_JSON;
    final String format = TimesController.PlatformTimeFormatRequestParamValues.UNIX_TIMESTAMP;

    final ResultActions resultActions = this.mockMvc.perform(
      get("/v1/platform-time")
        .accept(acceptableMediaType)
        .param("format",format)
    );
    
    assertGetPlatformTimeSuccessResponse(format, resultActions, acceptableMediaType);
  }

  /**
   * Tests making a valid request to the 'Get Platform Time' API, that excludes all optional request params, and
   * which results in a success response.
   * <p>
   * Serves to test the following additional integration points over and above those tested by
   * {@link #getPlatformTimeWhenValidRequestWithOptionalRequestParams()} -
   * <br/>
   * - Binding a default value to Controller's method arguments, when optional request parameters when not supplied.
   *
   * @throws Exception if an unexpected error occurs on execution of this test.
   */
  @Test
  void getPlatformTimeWhenMinimalValidRequest() throws Exception {
    final MediaType  acceptableMediaType = MediaType.APPLICATION_JSON;

    final ResultActions resultActions = this.mockMvc.perform(
      get("/v1/platform-time")
        .accept(acceptableMediaType)
    );

    assertGetPlatformTimeSuccessResponse(null, resultActions, acceptableMediaType);
  }

  /**
   * Tests making a request to the 'Get Platform Time' API, with an invalid 'format' request param.
   * <p>
   * This test case does NOT serve to test any additional integration points with the (Spring) Web MVC framework, and
   * in the purest sense it should be implemented as a unit test. Deferred implementing as separate unit test until
   * additional testing warrants a separate test class.
   *
   * @throws Exception if an unexpected error occurs on execution of this test.
   */
  @Test
  void getPlatformTimeWhenInvalidFormatRequestParam() throws Exception {
    final MediaType  acceptableMediaType = MediaType.APPLICATION_JSON;
    final String format = "invalid";

    final ResultActions resultActions = this.mockMvc.perform(
      get("/v1/platform-time")
        .accept(acceptableMediaType)
        .param("format","invalid")
    );

    assertGetPlatformTimeFailureResponse(resultActions, acceptableMediaType, HttpStatus.BAD_REQUEST,
      ApiErrorCode.INVALID_REQUEST_PARAM_VALUE.toString(), ".*format.*"+format+".*");
  }

  private void assertGetPlatformTimeSuccessResponse(String requestedFormat, ResultActions resultActions,
    MediaType expectedResponseContentType) throws Exception {
    // Uncomment the following line to dump the request for debugging purposes
    //resultActions.andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().isOk());
    resultActions.andExpect(content().contentTypeCompatibleWith(expectedResponseContentType));

    // This method only supports asserting JSON representations - output a friendly error message if the content-type
    // of body is unexpectedly something different
    resultActions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    final Instant now = Instant.now();
    if (TimesController.PlatformTimeFormatRequestParamValues.UNIX_TIMESTAMP.equals(requestedFormat)) {
      resultActions.andExpect(jsonPath("$.epochSeconds").exists());
      resultActions.andExpect(jsonPath("$.epochSeconds").isNumber());
      final String responseBody = resultActions.andReturn().getResponse().getContentAsString();
      final long actualPlatformTimeEpochSecs = JsonPath.parse(responseBody).read("$.epochSeconds", Long.class);
      assertThat(actualPlatformTimeEpochSecs).isLessThanOrEqualTo(now.getEpochSecond());
    } else if (requestedFormat == null ||
      requestedFormat.equals(TimesController.PlatformTimeFormatRequestParamValues.ISO_8601)) {
      resultActions.andExpect(jsonPath("$.dateTime").exists());
      resultActions.andExpect(jsonPath("$.dateTime").isString());
      final String responseBody = resultActions.andReturn().getResponse().getContentAsString();
      final String actualPlatformTimeDateTime = JsonPath.parse(responseBody).read("$.dateTime", String.class);
      assertThat(Instant.parse(actualPlatformTimeDateTime)).isAfterOrEqualTo(now.truncatedTo(ChronoUnit.SECONDS));
    } else {
      throw new AssertionError("Don't know how to assert success response for unknown requestedFormat ["+requestedFormat+"]");
    }

    // Defer more rigorous checking of the resource representation, such as its adherence to a schema to a
    // higher-level (Component) test, to avoid duplication of testing effort.
  }

  private void assertGetPlatformTimeFailureResponse(ResultActions resultActions, MediaType expectedResponseContentType,
    HttpStatus expectedHttpStatus, String expectedApiErrorCode, String expectedApiErrorMessageRegex) throws Exception {
    // Uncomment the following line to dump the request for debugging purposes
    //resultActions.andDo(MockMvcResultHandlers.print());

    resultActions.andExpect(status().is(expectedHttpStatus.value()));
    resultActions.andExpect(content().contentTypeCompatibleWith(expectedResponseContentType));

    // This method only supports asserting JSON representations - output a friendly error message if the content-type
    // of body is unexpectedly something different
    resultActions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    if (expectedApiErrorCode !=null) {
      resultActions.andExpect(jsonPath("$.code").value(expectedApiErrorCode));
    }
    if (expectedApiErrorMessageRegex !=null) {
      final String responseBody = resultActions.andReturn().getResponse().getContentAsString();
      final String actualMessage = JsonPath.parse(responseBody).read("$.message", String.class);
      assertThat(actualMessage).matches(expectedApiErrorMessageRegex);
    }
  }

}