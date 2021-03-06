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
 *     Anahide Tchertchian
 */
package org.nuxeo.ecm.web.resources.api;

import org.apache.commons.lang.StringUtils;

/**
 * @since 7.3
 */
public enum ResourceType {

    any, unknown, css, js, bundle, html, jsfjs, jsfcss, xhtml, xhtmlfirst;

    public String getSuffix() {
        return "." + name();
    }

    /**
     * @since 7.4
     */
    public final boolean equals(String type) {
        if (name().equalsIgnoreCase(type)) {
            return true;
        }
        return false;
    }

    public final boolean matches(Resource r) {
        if (ResourceType.any == this) {
            return true;
        }
        if (r == null || r.getType() == null) {
            return true;
        }
        if (this.name().toLowerCase().equals(r.getType().toLowerCase())) {
            return true;
        }
        return false;
    }

    public static final ResourceType parse(String type) {
        for (ResourceType item : values()) {
            if (item.name().equals(type)) {
                return item;
            }
        }
        return ResourceType.unknown;
    }

    public static final boolean matches(String type, Resource r) {
        if (StringUtils.isBlank(type) || ResourceType.any.name().equals(type.toLowerCase())) {
            return true;
        }
        String rt = r.getType();
        if (StringUtils.isBlank(rt)) {
            return true;
        }
        if (type.toLowerCase().equals(rt.toLowerCase())) {
            return true;
        }
        return false;
    }

}
