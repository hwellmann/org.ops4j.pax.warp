/*
 * Copyright 2014 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.warp.core.util;


/**
 * @author Harald Wellmann
 *
 */
public class WarpException extends RuntimeException {

    private static final long serialVersionUID = -5515301562862478256L;

    public WarpException() {
        super();
    }

    public WarpException(String message) {
        super(message);
    }

    public WarpException(String message, Throwable cause) {
        super(message, cause);
    }

    public WarpException(Throwable cause) {
        super(cause);
    }
}
