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

package org.simpliccity.sst.data.rest.security;

import static org.simpliccity.sst.service.security.ExtractorTypes.PARAMETER;

import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PropertyRepository extends JpaRepository<Property, String> 
{
	@SecuredMessage
	(
		restrictions = {@SemanticConstraint(semanticRule = "prefix", contentExtractor = PARAMETER, contentSpec = "name")}
	)
	public Property findByName(@Param("name") String name);
}
