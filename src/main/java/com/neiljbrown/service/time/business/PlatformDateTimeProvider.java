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
package com.neiljbrown.service.time.business;

import java.time.Instant;

/**
 * Provides a local business API for retrieving the platform's official, current date/time for use by other platform
 * services and clients.
 * <p>
 * The platform date time is always expressed and returned in the UTC time-zone.
 */
public interface PlatformDateTimeProvider {

  /**
   * @return an {@link Instant} representing the platform's current date and time (UTC).
   */
  Instant getDateTime();
}
