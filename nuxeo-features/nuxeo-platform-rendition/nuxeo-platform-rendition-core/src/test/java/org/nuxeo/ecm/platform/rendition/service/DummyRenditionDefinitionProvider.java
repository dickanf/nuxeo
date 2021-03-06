/*
 * (C) Copyright 2015 Nuxeo SA (http://nuxeo.com/) and others.
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
 *
 * Contributors:
 *     Thomas Roger
 */

package org.nuxeo.ecm.platform.rendition.service;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * @since 7.2
 */
public class DummyRenditionDefinitionProvider implements RenditionDefinitionProvider {

    @Override
    public List<RenditionDefinition> getRenditionDefinitions(DocumentModel doc) {
        List<RenditionDefinition> renditionDefinitions = new ArrayList<>();
        RenditionDefinition renditionDefinition = new RenditionDefinition();
        renditionDefinition.setName("dummyRendition1");
        renditionDefinitions.add(renditionDefinition);

        renditionDefinition = new RenditionDefinition();
        renditionDefinition.setName("dummyRendition2");
        renditionDefinitions.add(renditionDefinition);

        return renditionDefinitions;
    }

}
