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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

import org.springframework.stereotype.Service;

/**
 * A POJO implementation of {@link PlatformDateTimeProvider} for use in production.
 * <p>
 * Uses a {@link java.time.Clock} to derive the current platform time.
 */
@Service
public class PlatformDateTimeProviderImpl implements PlatformDateTimeProvider {

  private final Clock clock;

  /**
   * Creates an instance that uses the node's system clock as the source of the platform time.
   */
  public PlatformDateTimeProviderImpl() {
    this(Clock.systemUTC());
  }

  /**
   * Creates an instance that uses the supplied {@link Clock} as the source of the platform time.
   * <p>
   * Only provided to aid testing of other classes by allowing a {@link Clock#fixed fixed Clock} to be used where a
   * static time is needed.
   *
   * @param clock a {@link Clock}. Must be configured with a time zone of UTC.
   */
  PlatformDateTimeProviderImpl(Clock clock) {
    Objects.requireNonNull(clock,"Arg 'clock' must not be null.");
    if(!clock.getZone().equals(ZoneId.of("Z"))) {
      throw new IllegalArgumentException("Arg 'clock' must use a time zone of UTC, not [" + clock.getZone() + "].");
    }
    this.clock = clock;
  }

  @Override
  public Instant getDateTime() {
    return this.clock.instant();
  }
}
