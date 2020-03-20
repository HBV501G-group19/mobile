package is.hi.hopon.backend.Models.Core;

import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.util.ArrayUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import is.hi.hopon.backend.Models.Ride.Geo;

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
        Class objectList() default Object.class;
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

                Object val = null;
                Log.i("HLOG/lst", wrapper.objectList().getName());
                if(wrapper.objectList().getName().startsWith("is.hi.hopon.backend.Models.")){
                //if(wrapper.objectList().getClass().isAssignableFrom(Model.class)) {
                    Log.i("HLOG/fromJsonArray", jsonField);
                    JSONArray array = data.getJSONArray(jsonField);
                    List<Object> list = new ArrayList<Object>();
                    for(int i=0; i< array.length(); i++){
                        Log.i("HLOG/from", "adding " + i);
                        Object e = array.get(i);
                        list.add(e);

                        try {
                            Method dejsonfier = wrapper.objectList().getMethod("fromJson", JSONObject.class);
                            Object no = wrapper.objectList().newInstance();
                            Object res = dejsonfier.invoke(no, e);
                            Log.i("HLOG/from", "Results in " + res);

                        }catch(NoSuchMethodException |InstantiationException | InvocationTargetException nex) {
                            Log.w("HLOG/from", "Reflective fromJson " + nex.toString());
                        }


                        Log.i("HLOG/from", "Elem " + e.getClass().toString() + e.toString());
                        //Method dejsonfier = e.getClass().getMethod("fromJson");
                        //JSONObject jo = (JSONObject) jsonfirier.invoke(e);
                        //Log.i("HLOG/toJson", e.getClass().toString() + " " + jo.toString());
                        //array.put(jo);



                    }
                    val = list;
                }
                else {
                    val = data.get(jsonField);
                }
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
                Log.i("HLOG/toJson", "0\t\t" + jsonField);
                if (wrapper.required() && (val == null || val.toString().isEmpty())) {
                    throw new SerializationError(jsonField + " is required");
                }
                if(field.getType().getSuperclass() == Model.class){
                    Log.i("HLOG/submodel", "1");
                    json.put(jsonField, ((Model)val).toJson());
                }
                else if(wrapper.objectList().isAssignableFrom(Model.class))
                {
                    Log.i("HLOG/toJson", jsonField + "\t2\t" + val);

                    JSONArray array = new JSONArray();
                    for(Object e: (List<Object>)val)
                    {
                        try {
                            Method jsonfirier = e.getClass().getMethod("toJson");
                            JSONObject jo = (JSONObject) jsonfirier.invoke(e);
                            Log.i("HLOG/toJson", e.getClass().toString() + " " + jo.toString());
                            array.put(jo);
                            Log.i("HLOG/toJson", "x");
                        }catch(NoSuchMethodException | InvocationTargetException er)
                        {
                            array.put(e);
                            Log.w("HLOG/tojson", "Missing on " + e.getClass().toString() + " " + e + "\t"+ er.toString());
                        }
                    }
                    json.put(jsonField, array);
                }
                else {
                    json.put(jsonField, val);
                }
                field.setAccessible(accessible);
            } catch (IllegalAccessException | JSONException x) {
                throw new SerializationError(x.toString());
            }
        }
        Log.i("HLOG/toJson", "Returning " + json);
        return json;
    }
}
