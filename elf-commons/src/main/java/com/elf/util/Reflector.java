/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author bnevins
 * December 2015
 */
public class Reflector {
   
}

class ClassInfo {
    Class clazz;
    List<Method> methods;
    List<Field> fields;
}

class MethodInfo {
    Method method;
}

class FieldInfox {
    Field field;
}

class ReflectorUtils {
    static boolean isGetter(Method m) {
        throw new RuntimeException("not implemented");
    }
}