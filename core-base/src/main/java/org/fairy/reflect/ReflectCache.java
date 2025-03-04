/*
 * MIT License
 *
 * Copyright (c) 2021 Imanity
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.fairy.reflect;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.fairy.util.Utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ReflectCache {

    private static final LoadingCache<Class<?>, ReflectCache> CLASS_ACCESSORS = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(ReflectCache::new);

    public static ReflectCache get(Class<?> parentClass) {
        return CLASS_ACCESSORS.get(parentClass);
    }

    private final Class<?> parentClass;
    private final Map<ReflectQuery, Method> methodCache;
    private final Map<ReflectQuery, Field> fieldCache;

    public ReflectCache(Class<?> parentClass) {
        this.parentClass = parentClass;

        this.methodCache = new ConcurrentHashMap<>();
        this.fieldCache = new ConcurrentHashMap<>();
    }

    public Method resolveMethod(ReflectQuery query) {
        if (methodCache.containsKey(query)) {
            return methodCache.get(query);
        }

        int currentIndex = 0;
        Method result = null;

        List<Method> toSearch;
        if (query.getModifier() == -1) {
            toSearch = Reflect.getDeclaredMethods(this.parentClass);
        } else {
            toSearch = Reflect.getDeclaredMethods(this.parentClass, query.getModifier(), true);
        }

        for (Method method : toSearch) {
            if ((query.getReturnType() == null || Utility.wrapPrimitive(query.getReturnType()).equals(Utility.wrapPrimitive(method.getReturnType())))
                    && (query.getName() == null || method.getName().equals(query.getName()))) {
                if (query.getTypes() != null && query.getTypes().length > 0) {
                    if (!Utility.isParametersEquals(method.getParameterTypes(), query.getTypes())) {
                        continue;
                    }
                }

                if (query.getIndex() == -2) {
                    result = method;
                    continue;
                }

                if (query.getIndex() < 0 || query.getIndex() == currentIndex++) {
                    try {
                        return this.cache(query, method);
                    } catch (ReflectiveOperationException ex) {
                        throw new ImanityReflectException("Unable to set accessible", ex);
                    }
                }
            }
        }
        if (result != null) {
            try {
                return this.cache(query, result);
            } catch (ReflectiveOperationException ex) {
                throw new ImanityReflectException("Unable to set accessible", ex);
            }
        }

        throw new ImanityReflectException("No Such Method " + query.toString());
    }

    public Field resolveField(ReflectQuery query) {
        if (fieldCache.containsKey(query)) {
            return fieldCache.get(query);
        }

        int currentIndex = 0;
        Field result = null;

        List<Field> toSearch;
        if (query.getModifier() == -1) {
            toSearch = Reflect.getDeclaredFields(this.parentClass);
        } else {
            toSearch = Reflect.getDeclaredFields(this.parentClass, query.getModifier(), true);
        }

        for (Field field : toSearch) {
            if ((query.getName() == null || field.getName().equals(query.getName()))
                    && (query.getReturnType() == null || Utility.wrapPrimitive(query.getReturnType()).equals(Utility.wrapPrimitive(field.getType())))) {
                if (query.getTypes() != null && query.getTypes().length > 0) {
                    Type[] genericTypes = Utility.getGenericTypes(field);
                    if (genericTypes == null) {
                        continue;
                    }

                    if (!Utility.isParametersEquals(genericTypes, query.getTypes())) {
                        continue;
                    }
                }

                if (query.getIndex() == -2) {
                    result = field;
                    continue;
                }

                if (query.getIndex() < 0 || query.getIndex() == currentIndex++) {
                    try {
                        return this.cache(query, field);
                    } catch (ReflectiveOperationException ex) {
                        throw new ImanityReflectException("Unable to set accessible", ex);
                    }
                }
            }
        }

        if (result != null) {
            try {
                return this.cache(query, result);
            } catch (ReflectiveOperationException ex) {
                throw new ImanityReflectException("Unable to set accessible", ex);
            }
        }
        throw new ImanityReflectException("No Such Field " + query.toString());
    }

    private Method cache(ReflectQuery query, Method method) throws ReflectiveOperationException {
        Reflect.setAccessible(method);
        this.methodCache.put(query, method);
        return method;
    }

    private Field cache(ReflectQuery query, Field field) throws ReflectiveOperationException {
        Reflect.setAccessible(field);
        this.fieldCache.put(query, field);
        return field;
    }
}
