/*
 * The MIT License
 *
 *  Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.dnastack.beacon.core.adapter.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * This class simply is acting as a datastorage for the BeaconAdapter to perform lookups on
 *
 * @author patmagee
 */
public class SampleDataStore {

    private final Map<String, Map> DATA;

    public SampleDataStore() {
        DATA = new HashMap<>();
        Map<String, Map> assemblies = new HashMap<>();
        Map<String, Map> references = new HashMap<>();
        Map<Long, Map> positions = new HashMap<>();
        Map<String, String> bases = new HashMap<>();
        bases.put("referenceBases", "A");
        bases.put("alternateBases", "C");
        positions.put(1000l, bases);
        references.put("1", positions);
        assemblies.put("GRCh37", references);
        DATA.put(SampleBeaconAdapterImpl.DATASET_ID, assemblies);

    }

    public Map<String, Map> getDATA() {
        return DATA;
    }

}
