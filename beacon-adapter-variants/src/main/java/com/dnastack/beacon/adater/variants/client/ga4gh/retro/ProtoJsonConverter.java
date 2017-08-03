package com.dnastack.beacon.adater.variants.client.ga4gh.retro;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.reflect.MethodUtils;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * A simple converter Json <-> protobuf DTOs.
 *
 * @author Artem (tema.voskoboynick@gmail.com)
 * @author Miro Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ProtoJsonConverter extends Converter.Factory {

    public static ProtoJsonConverter create() {
        return new ProtoJsonConverter();
    }

    private boolean isConvertible(Type type) {
        return getConvertibleClass(type) != null;
    }

    private Class<?> getConvertibleClass(Type type) {
        if (!(type instanceof Class)) {
            return null;
        }

        Class<?> clazz = (Class) type;
        if (!(MessageLite.class.isAssignableFrom(clazz))) {
            return null;
        }

        return clazz;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Class<?> clazz = getConvertibleClass(type);
        if (clazz == null) {
            return null;
        }

        return new Converter<ResponseBody, Object>() {

            private GeneratedMessage.Builder createBuilder(Class<?> clazz) {
                try {
                    return (GeneratedMessage.Builder) MethodUtils.invokeStaticMethod(clazz, "newBuilder");
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Couldn't create builder", e);
                }
            }

            @Override
            public Object convert(ResponseBody responseBody) throws IOException {
                String json = responseBody.string();
                GeneratedMessage.Builder builder = createBuilder(clazz);
                JsonFormat.parser().merge(json, builder);

                return builder.build();
            }
        };
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (!isConvertible(type)) {
            return null;
        }

        return o -> {
            MessageOrBuilder message = (MessageOrBuilder) o;
            String json = JsonFormat.printer().print(message);

            return RequestBody.create(MediaType.parse("application/json"), json);
        };
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return super.stringConverter(type, annotations, retrofit);
    }
}
