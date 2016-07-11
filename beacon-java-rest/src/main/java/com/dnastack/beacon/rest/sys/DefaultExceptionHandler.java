/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.beacon.rest.sys;

import org.ga4gh.beacon.BeaconError;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Default Exception handler to catch all Non Beacon exceptions thrown by the rest resources.
 *
 * @author patmagee
 * @author Artem (tema.voskoboynick@gmail.com)
 */
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Throwable> {

    /**
     * Default error handler to capture all errors and send the user a JSON response
     * with the status code, reason, message and stacktrace of the error
     *
     * @return response with the status and error entity
     */
    @Override
    public Response toResponse(Throwable exception) {
        BeaconError response = BeaconError.newBuilder()
                .setMessage(exception.getMessage())
                .setErrorCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .build();

        return Response.status(response.getErrorCode()).type(MediaType.APPLICATION_JSON).entity(response).build();
    }
}
