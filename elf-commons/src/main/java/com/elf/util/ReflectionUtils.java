package com.elf.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 */
public class ReflectionUtils {

    public int test;
    public int test2;
    @FooGoo
    private int imprivate;

    @FooGoo
    String getAllAnnotations(Class clazz) {
        StringBuilder sb = new StringBuilder();
        Annotation[] annotations = clazz.getAnnotations();
        sb.append(getAnnotations(clazz));

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            sb.append(getAnnotations(method));
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            sb.append(getAnnotations(field));
        }

        return sb.toString();
    }

    @Deprecated
    @HasArgs(length=100, name="foo")
    String getAnnotations(Class clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        String ret = "FOUND " + annotations.length + " Annotations at class scope in " + clazz.getSimpleName() + "\n";
        ret += getAnnotations(annotations);
        return ret;
    }

    String getAnnotations(Method method) {
        Annotation[] annotations = method.getAnnotations();
        String ret = "FOUND " + annotations.length + " Annotations at method scope for " + method.getName() + "\n";
        ret += getAnnotations(annotations);
        return ret;
    }

    String getAnnotations(Field field) {
        Annotation[] annotations = field.getAnnotations();
        String ret = "FOUND " + annotations.length + " Annotations at field scope for " + field.getName() + "\n";
        ret += getAnnotations(annotations);
        return ret;
    }

    private String getAnnotations(Annotation[] annotations) {
        // todo stringbuilder
        String ret = "";

        for (Annotation annotation : annotations) {
            ret += "    " + annotation.toString() + "\n";
        }
        return ret;
    }

    @FooGoo
    @Hello
    @Qbert
    public static void main(String[] args) {
        try {
            System.out.println(System.getProperty("java.class.path"));
            //Class clazz = Class.forName("com.elf.util.logging.Reporter");
            //Class clazz = Class.forName("com.elf.util.Classpath");
            Class clazz = Class.forName("com.elf.util.Child");
            System.out.println(new ReflectionUtils().getAllAnnotations(clazz));
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }
}

@Retention(value = RUNTIME)
@interface FooGoo {
}

@Retention(value = RUNTIME)
@interface Qbert {
}

@Retention(value = RUNTIME)
@interface Hello {
}

@Retention(value = RUNTIME)
@interface HasArgs {

    String name();

    int length();
}


class Child extends ReflectionUtils {
    @Override
            @FooGoo
    String getAnnotations(Field field) {
        return "";
    }
}