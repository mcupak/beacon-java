package com.dnastack.beacon.rest.providers.util;


import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by patrickmagee on 2016-06-16.
 */
public class ResponseMapping {

    private static final Map<String, Response.Status> mapping;

    static {
        Map<String, Response.Status> map = new HashMap<>();
        map.put(NotFoundException.class.getCanonicalName(), Response.Status.NOT_FOUND);
        map.put(NoResultException.class.getCanonicalName(), Response.Status.NOT_FOUND);
        map.put(IllegalArgumentException.class.getCanonicalName(), Response.Status.BAD_REQUEST);
        map.put(UnsupportedOperationException.class.getCanonicalName(), Response.Status.METHOD_NOT_ALLOWED);
        map.put(SecurityException.class.getCanonicalName(), Response.Status.FORBIDDEN);
        mapping = Collections.unmodifiableMap(map);
    }

    /**
     * Retrieves Response.Status from a given exception according to the static mapping. If the mapping does not contain
     * the exception, INTERNAL_SERVER_ERROR is returned.
     *
     * @param ex exception
     *
     * @return response status
     */
    public static Response.Status getStatus(Exception ex) {
        Response.Status s = mapping.get(ex.getClass().getCanonicalName());

        return (s == null) ? Response.Status.INTERNAL_SERVER_ERROR : s;
    }
}
