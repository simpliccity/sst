/*
 *    Copyright 2016 Information Control Corporation
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
 * Defines a simple framework (<code>jwt-client</code>) for generating JSON Web Tokens
 * and incorporating them in the headers of HTTP service requests.
 * Employs the JJWT library for manipulating JSON Web Tokens.
 * 
 * @see <a href="https://tools.ietf.org/html/rfc7519">JSON Web Token specification (RFC 7519)</a>
 * @see <a href="https://github.com/jwtk/jjwt">JJWT Library</a>
 * 
 * @since 1.0.0
 */
package org.simpliccity.sst.web.security.jwt;