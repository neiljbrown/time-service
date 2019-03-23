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

package com.neiljbrown.service.time.presentation;

/**
 * The set of possible error codes returned by this application's APIs.
 * <p>
 * An error code is a unique string which describes the type of error, written in camel-case format, e.g.
 * AuthenticationFailed. (Strings are used instead of numbers to aid human interpretation / readability).
 * <p>
 * An error code is used to communicate the cause of an error back to (API) clients, so they can react appropriately. It
 * can also potentially be used to drive the runtime lookup of a separate 'display' message suitable for end users,
 * whether they be API clients or humans accessing the application via a UI.
 */
public enum ApiErrorCode {

  INVALID_REQUEST_PARAM_VALUE
}
