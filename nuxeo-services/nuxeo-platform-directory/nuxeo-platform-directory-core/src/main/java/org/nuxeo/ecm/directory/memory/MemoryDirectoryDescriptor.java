/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Anahide Tchertchian
 */
package org.nuxeo.ecm.directory.memory;

import java.util.Set;

import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.directory.Directory;
import org.nuxeo.ecm.directory.BaseDirectoryDescriptor;

/**
 * @since 5.6
 */
@XObject("directory")
public class MemoryDirectoryDescriptor extends BaseDirectoryDescriptor {

    public Set<String> schemaSet;

    @Override
    public Directory newDirectory() {
        if (schemaSet == null) {
            return new MemoryDirectory(name, schemaName, idField, passwordField);
        } else {
            return new MemoryDirectory(name, schemaName, schemaSet, idField, passwordField);
        }
    }

}