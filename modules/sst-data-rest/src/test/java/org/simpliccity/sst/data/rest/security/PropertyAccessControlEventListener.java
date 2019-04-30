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

import static org.simpliccity.sst.service.security.ExtractorTypes.PAYLOAD_SPEL;

import org.simpliccity.sst.data.rest.security.AbstractAccessControlRepositoryEventListener;
import org.simpliccity.sst.service.security.ServiceAccessException;
import org.simpliccity.sst.service.security.annotation.SecuredMessage;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.stereotype.Component;

@Component
public class PropertyAccessControlEventListener extends	AbstractAccessControlRepositoryEventListener 
{
	@HandleBeforeDelete
	@SecuredMessage
	(
		restrictions = {@SemanticConstraint(semanticRule = "prefix", contentExtractor = PAYLOAD_SPEL, contentSpec = "name")}
	)
	public void handleBeforeDelete(Property property) throws ServiceAccessException
	{
		checkRepositoryEventAccess(property, HandleBeforeDelete.class);
	}
}
