package com.dnastack.beacon.adapter.variants.utils

import com.google.protobuf.util.JsonFormat

/**
 * Json helper that is able to correctly convert protobuf objects to json.
 *
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
public class Json {

    public static String toJson(Object src) {
        return JsonFormat.printer().print(src)
    }
}