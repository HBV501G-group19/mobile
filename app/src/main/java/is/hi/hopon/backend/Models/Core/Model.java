package is.hi.hopon.backend.Models.Core;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;

public class Model {

    public Model() {
    }

    public static class SerializationError extends Exception {
        public SerializationError(String errorMessage) {
            super(errorMessage);
        }
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Field {
        String name() default "";

        boolean required() default false;
    }

    public <T> T fromJson(JSONObject data) throws SerializationError {
        T model = (T)this;
        for (java.lang.reflect.Field field : model.getClass().getDeclaredFields()) {
            Field wrapper = field.getAnnotation(Field.class);
            if (wrapper == null) continue;
            try {
                String jsonField = wrapper.name() == "" ? field.getName() : wrapper.name();
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Object val = data.get(jsonField);
                if (wrapper.required() && (val == null || val.toString().isEmpty())) {
                    throw new SerializationError("Invalid payload, missing value for " + jsonField);
                }
                field.set(model, val);
                field.setAccessible(accessible);
            } catch (IllegalAccessException | JSONException x) {
                throw new SerializationError(x.toString());
            }
        }
        return model;
    }

    public JSONObject toJson() throws SerializationError {
        JSONObject json = new JSONObject();
        for (java.lang.reflect.Field field : this.getClass().getDeclaredFields()) {
            Field wrapper = field.getAnnotation(Field.class);
            if (wrapper == null) continue;
            try {
                String jsonField = wrapper.name() == "" ? field.getName() : wrapper.name();
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Object val = field.get(this);
                if (wrapper.required() && (val == null || val.toString().isEmpty())) {
                    throw new SerializationError(jsonField + " is required");
                }
                json.put(jsonField, val);
                field.setAccessible(accessible);
            } catch (IllegalAccessException | JSONException x) {
                throw new SerializationError(x.toString());
            }
        }
        return json;
    }
}
