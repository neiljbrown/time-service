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
package com.neiljbrown.service.time.business;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Unit tests for {@link PlatformDateTimeProviderImpl}.
 */
class PlatformDateTimeProviderImplTest {

  private static final ZoneId ZONE_ID_UTC = ZoneId.of("Z");

  private PlatformDateTimeProviderImpl dateTimeProvider;

  @BeforeEach
  void setUp() {
    this.dateTimeProvider = createPlatformDateTimeProviderWithDefaultClock();
  }

  /**
   * Tests creating an instance from a supplied Clock using
   * {@link PlatformDateTimeProviderImpl#PlatformDateTimeProviderImpl(Clock)} in the case where the Clock uses the
   * UTC time zone.
   */
  @Test
  void createFromUtcClock() {
    new PlatformDateTimeProviderImpl(createFixedClock(ZONE_ID_UTC));
  }

  /**
   * Tests creating an instance from a supplied Clock using
   * {@link PlatformDateTimeProviderImpl#PlatformDateTimeProviderImpl(Clock)} in the case where the Clock uses the
   * a time zone which is NOT UTC.
   */
  @Test
  void createFromNonUtcClock() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new PlatformDateTimeProviderImpl(createFixedClock(ZoneId.of("Europe/Paris"))))
      .withMessageContaining("UTC");
  }


  /**
   * Tests {@link PlatformDateTimeProviderImpl#getDateTime()} using an instance that is created from the default
   * Clock, using the default constructor.
   *
   * @throws Exception if an unexpected error occurs.
   */
  @Test
  void getDateTimeWhenUsingDefaultClock() throws Exception {
    final Instant dateTime = this.dateTimeProvider.getDateTime();

    assertThat(dateTime).isNotNull();
    Thread.sleep(1);
    final Instant newDateTime = this.dateTimeProvider.getDateTime();
    assertThat(newDateTime).isAfter(dateTime);
  }

  /**
   * Tests {@link PlatformDateTimeProviderImpl#getDateTime()} using an instance that is created from a supplied
   * (non-default) Clock.
   */
  @Test
  void getDateTimeWhenUsingNonDefaultClock() {
    final Clock fixedClock = createFixedClock(ZONE_ID_UTC);
    this.dateTimeProvider = new PlatformDateTimeProviderImpl(fixedClock);

    Instant instant = dateTimeProvider.getDateTime();
    assertThat(instant).isNotNull();
    assertThat(instant).isEqualTo(fixedClock.instant());
  }

  private PlatformDateTimeProviderImpl createPlatformDateTimeProviderWithDefaultClock() {
    return new PlatformDateTimeProviderImpl();
  }

  private Clock createFixedClock(ZoneId zoneId) {
    return Clock.fixed(Instant.now(), zoneId);
  }
}