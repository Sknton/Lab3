package org.fpm.di.example;

import org.fpm.di.Container;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DummyContainer implements Container {
    DummyBinder binder;

    public DummyContainer(DummyBinder dummyBinder) {
        this.binder = dummyBinder;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        T singletonObject = binder.singletons(clazz);
        if(singletonObject != null) return singletonObject;

        if(binder.getClass(clazz) != null) return constructObject(clazz);

        Class<? extends T> implementation = binder.injections(clazz);
        if(implementation != null){
            T t = getComponent(implementation);
            if(t != null){
                return t;
            }
            return constructObject(implementation);
        }

        return null;
    }





    private <T> T constructObject(Class<T> clazz){
        for(Constructor<?> constructor: clazz.getConstructors()) {
            if(constructor.getAnnotation(Inject.class) != null){
                try {
                    return (T) constructor.newInstance(getComponent(constructor.getParameterTypes()[0]));
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }



}
