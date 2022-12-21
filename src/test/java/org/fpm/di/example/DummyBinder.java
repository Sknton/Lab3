package org.fpm.di.example;

import org.fpm.di.Binder;

import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class DummyBinder implements Binder {

    @Override
    public <T> void bind(Class<T> clazz) {
        if(clazz.getAnnotation(Singleton.class) != null){
            try {
                Constructor<T> constructor = clazz.getConstructor();
                bind(clazz, constructor.newInstance());
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        classes.add(clazz);
    }


    ArrayList<Class<?>> classes = new ArrayList<>();
    HashMap<Class<?>, Class<?>> injectionDependencies = new HashMap<>();
    HashMap<Class<?>,Object> singletones = new HashMap<>();

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        injectionDependencies.put(clazz, implementation);
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        singletones.put(clazz, instance);
    }

    public<T> Class<T> getClass(Class<T> initial){
        if(classes.contains(initial))
            return initial;
        return null;
    }

    public<T> Class<? extends T> injections(Class<T> myClass){
        if(injectionDependencies.containsKey(myClass))
            return (Class<? extends T>) injectionDependencies.get(myClass);
        return null;
    }

    public<T> T singletons(Class<T> myClass){
        if(singletones.containsKey(myClass))
            return (T) singletones.get(myClass);
        return null;
    }
}
