package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {


    public static void injectObjects(Object target, String fieldname, Object toinject) {

        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldname);

            if (!field.isAccessible()) {
                field.setAccessible(true);
                wasPrivate = true;

            }

            field.set(target, toinject);

            if (wasPrivate) {
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
           e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
