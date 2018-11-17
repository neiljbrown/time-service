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

package com.neiljbrown.service.time.presentation.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neiljbrown.service.time.business.PlatformDateTimeProvider;
import com.neiljbrown.service.time.presentation.ApiErrorCode;

/**
 * {@link RestController Controller for RESTful web API} that handles requests for operations on Time (date and) related
 * API resources.
 * <p>
 * Singleton. Controller handling methods must be thread-safe.
 */
@RestController
public class TimesController {

  private PlatformDateTimeProvider platformDateTimeProvider;

  /** Set of valid values for the requested format in which the Platform time can be returned. */
  // Implemented as set of string constants rather than enum type as latter is incompatible with @RequestParam
  // defaultValue, which has to be a string constant.
  static class PlatformTimeFormatRequestParamValues {
    static final String ISO_8601 = "iso-8601";
    static final String UNIX_TIMESTAMP = "unix";
    static final Set<String> ALL_FORMATS = new HashSet<>();
    static {
      ALL_FORMATS.add(ISO_8601);
      ALL_FORMATS.add(UNIX_TIMESTAMP);
    }
  }

  /**
   * @param platformDateTimeProvider instance of {@link PlatformDateTimeProvider} used to obtain current platform
   * date/time.
   */
  public TimesController(PlatformDateTimeProvider platformDateTimeProvider) {
    this.platformDateTimeProvider = platformDateTimeProvider;
  }

  /**
   * Handles a request to retrieve a representation of the Platform date time - the official, current time for the
   * whole platform (system), always expressed in the UTC time-zone.
   *
   * @param format the format in which the Platform time should be returned. Optional. One of {@link
   * PlatformTimeFormatRequestParamValues#ISO_8601} or {@link PlatformTimeFormatRequestParamValues#UNIX_TIMESTAMP}. If
   * not specified defaults to {@link PlatformTimeFormatRequestParamValues#ISO_8601}.
   *
   * @return a {@link ResponseEntity}. If the request was successful, as indicated by a status code of 200, the body
   * contains the Platform time. Else the body contains an error representation detailing why the request failed.
   */
  @GetMapping(value = "/v1/platform-time", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map> getPlatformTime(
    @RequestParam(value = "format", required = false, defaultValue = PlatformTimeFormatRequestParamValues.ISO_8601) String format) {
    Map<String, Object> apiResource = new HashMap<>();
    if (isValidTimeFormat(format)) {
      Instant currentDateTime = this.platformDateTimeProvider.getDateTime();
      if (format.equalsIgnoreCase(PlatformTimeFormatRequestParamValues.ISO_8601)) {
        currentDateTime = currentDateTime.truncatedTo(ChronoUnit.SECONDS);
        apiResource.put("dateTime", currentDateTime.toString());
      } else if (format.equalsIgnoreCase(PlatformTimeFormatRequestParamValues.UNIX_TIMESTAMP)) {
        apiResource.put("epochSeconds", currentDateTime.getEpochSecond());
      } else {
        throw new AssertionError("Logic error. Don't know how to handle 'format' request param value [" + format + "].");
      }
      return ResponseEntity.ok(apiResource);
    } else {
      apiResource.put("code", ApiErrorCode.INVALID_REQUEST_PARAM_VALUE.toString());
      apiResource.put("message", "Invalid 'format' request param [" + format + "].");
      return new ResponseEntity<>(apiResource, HttpStatus.BAD_REQUEST);
    }
  }

  private static boolean isValidTimeFormat(String format) {
    return PlatformTimeFormatRequestParamValues.ALL_FORMATS.contains(format);
  }
}