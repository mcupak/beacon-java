/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dnastack.beacon.rest.util;

import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response.Status;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapping of exceptions to status codes.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ResponseMappingResource {

    private static final Map<String, Status> mapping;

    static {
        Map<String, Status> map = new HashMap<>();
        map.put(NotFoundException.class.getCanonicalName(), Status.NOT_FOUND);
        map.put(NoResultException.class.getCanonicalName(), Status.NOT_FOUND);
        map.put(IllegalArgumentException.class.getCanonicalName(), Status.BAD_REQUEST);
        map.put(UnsupportedOperationException.class.getCanonicalName(), Status.METHOD_NOT_ALLOWED);
        map.put(SecurityException.class.getCanonicalName(), Status.FORBIDDEN);
        mapping = Collections.unmodifiableMap(map);
    }

    /**
     * Retrieves Response.Status from a given exceptions according to the static mapping. If the mapping does not contain
     * the exceptions, INTERNAL_SERVER_ERROR is returned.
     *
     * @param ex exceptions
     * @return response status
     */
    public static Status getStatus(Exception ex) {
        Status s = mapping.get(ex.getClass().getCanonicalName());

        return (s == null) ? Status.INTERNAL_SERVER_ERROR : s;
    }
}
