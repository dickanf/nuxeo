/*
 * (C) Copyright 2006-2010 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     bstefanescu
 */
package org.nuxeo.connect.update;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class PackageValidationException extends PackageException {

    private static final long serialVersionUID = 1L;

    protected ValidationStatus status;

    public PackageValidationException(ValidationStatus status) {
        super("Validation exception: " + statusToString(status));
        this.status = status;
    }

    public ValidationStatus getStatus() {
        return status;
    }

    public static String statusToString(ValidationStatus status) {
        final StringBuilder buf = new StringBuilder();

        buf.append("PackageValidationException");
        buf.append(" {");
        buf.append(" errors=");
        buf.append(status.getErrors());
        buf.append(", warnings=");
        buf.append(status.getWarnings());
        buf.append('}');

        return buf.toString();
    }

}
