/*
 *    Copyright 2016 Information Control Company
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <p>Defines a simple, extensible framework (<code>access</code>) for applying access controls to service
 * operations based on the semantics of the service request.  The key elements of the framework include:</p>
 * <ul>
 * <li>Annotations that define access control constraints relating request semantics to the authenticated
 * principal placing the service call.</li>
 * <li>An access control rule abstraction that supports simple implementation of custom constraints.</li>
 * <li>Service request context and semantic content extraction abstractions that allow the framework to 
 * be adapted to various service implementation approaches.</li>
 * <li>A security handler abstraction that allows the framework to be adapted for use with different
 * security frameworks.</li>
 * <li>An abstract base class which enforces the access control rules for a service request.  This class
 * can be extended to incorporate the framework into the appropriate service implementation approach.  When
 * used, service requests must satisfy the constraints specified by annotations on the corresponding endpoint 
 * implementation.</li>
 * </ul>
 */
package org.simpliccity.sst.service.security;