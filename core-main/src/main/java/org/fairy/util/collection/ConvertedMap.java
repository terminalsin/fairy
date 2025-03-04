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

package org.fairy.util.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * Represents a map that wraps another map by transforming the entries going in and out.
 *
 * @author Kristian
 *
 * @param <VInner> - type of the value in the entries in the inner invisible map.
 * @param <VOuter> - type of the value in the entries publically accessible in the outer map.
 */
public abstract class ConvertedMap<Key, VInner, VOuter> extends AbstractConverted<VInner, VOuter> implements Map<Key, VOuter> {
    // Inner map
    private Map<Key, VInner> inner;

    // Inner conversion
    private BiFunction<Key, VOuter, VInner> innerConverter = new BiFunction<Key, VOuter, VInner>() {
        @Override
        public VInner apply(Key key, VOuter outer) {
            return toInner(key, outer);
        }
    };

    // Outer conversion
    private BiFunction<Key, VInner, VOuter> outerConverter = new BiFunction<Key, VInner, VOuter>() {
        @Override
        public VOuter apply(Key key, VInner inner) {
            return toOuter(key, inner);
        }
    };

    public ConvertedMap(Map<Key, VInner> inner) {
        if (inner == null)
            throw new IllegalArgumentException("Inner map cannot be NULL.");
        this.inner = inner;
    }

    @Override
    public void clear() {
        inner.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return inner.containsKey(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsValue(Object value) {
        return inner.containsValue(toInner((VOuter) value));
    }

    @Override
    public Set<Entry<Key, VOuter>> entrySet() {
        return convertedEntrySet(inner.entrySet(), innerConverter, outerConverter);
    }

    /**
     * Convert a value from the inner map to the outer visible map.
     * @param key - unused value.
     * @param inner - the inner value.
     * @return The outer value.
     */
    protected VOuter toOuter(Key key, VInner inner) {
        return toOuter(inner);
    }

    /**
     * Convert a value from the outer map to the internal inner map.
     * @param key - unused value.
     * @param outer - the outer value.
     * @return The inner value.
     */
    protected VInner toInner(Key key, VOuter outer) {
        return toInner(outer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public VOuter get(Object key) {
        return toOuter((Key) key, inner.get(key));
    }

    @Override
    public boolean isEmpty() {
        return inner.isEmpty();
    }

    @Override
    public Set<Key> keySet() {
        return inner.keySet();
    }

    @Override
    public VOuter put(Key key, VOuter value) {
        return toOuter(key, inner.put(key, toInner(key, value)));
    }

    @Override
    public void putAll(Map<? extends Key, ? extends VOuter> m) {
        for (Entry<? extends Key, ? extends VOuter> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public VOuter remove(Object key) {
        return toOuter((Key) key, inner.remove(key));
    }

    @Override
    public int size() {
        return inner.size();
    }

    @Override
    public Collection<VOuter> values() {
        return Collections2.transform(entrySet(), new Function<Entry<Key, VOuter>, VOuter>() {
            @Override
            public VOuter apply(@Nullable Entry<Key, VOuter> entry) {
                return entry.getValue();
            }
        });
    }

    /**
     * Returns a string representation of this map.  The string representation
     * consists of a list of key-value mappings in the order returned by the
     * map's <tt>entrySet</tt> view's iterator, enclosed in braces
     * (<tt>"{}"</tt>).  Adjacent mappings are separated by the characters
     * <tt>", "</tt> (comma and space).  Each key-value mapping is rendered as
     * the key followed by an equals sign (<tt>"="</tt>) followed by the
     * associated value.  Keys and values are converted to strings as by
     * {@link String#valueOf(Object)}.
     *
     * @return a string representation of this map
     */
    @Override
    public String toString() {
        Iterator<Entry<Key, VOuter>> i = entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Entry<Key, VOuter> e = i.next();
            Key key = e.getKey();
            VOuter value = e.getValue();
            sb.append(key   == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(", ");
        }
    }

    /**
     * Convert a collection of entries.
     * @param entries - the collection of entries.
     * @param innerFunction - the inner entry converter.
     * @param outerFunction - the outer entry converter.
     * @return The converted set of entries.
     */
    static <Key, VInner, VOuter> Set<Entry<Key, VOuter>> convertedEntrySet(
            final Collection<Entry<Key, VInner>> entries,
            final BiFunction<Key, VOuter, VInner> innerFunction,
            final BiFunction<Key, VInner, VOuter> outerFunction) {

        return new ConvertedSet<Entry<Key,VInner>, Entry<Key,VOuter>>(entries) {
            @Override
            protected Entry<Key, VInner> toInner(final Entry<Key, VOuter> outer) {
                return new Entry<Key, VInner>() {
                    @Override
                    public Key getKey() {
                        return outer.getKey();
                    }

                    @Override
                    public VInner getValue() {
                        return innerFunction.apply(getKey(), outer.getValue());
                    }

                    @Override
                    public VInner setValue(VInner value) {
                        return innerFunction.apply(getKey(), outer.setValue(outerFunction.apply(getKey(), value)));
                    }

                    @Override
                    public String toString() {
                        return String.format("\"%s\": %s", getKey(), getValue());
                    }
                };
            }

            @Override
            protected Entry<Key, VOuter> toOuter(final Entry<Key, VInner> inner) {
                return new Entry<Key, VOuter>() {
                    @Override
                    public Key getKey() {
                        return inner.getKey();
                    }

                    @Override
                    public VOuter getValue() {
                        return outerFunction.apply(getKey(), inner.getValue());
                    }

                    @Override
                    public VOuter setValue(VOuter value) {
                        final VInner converted = innerFunction.apply(getKey(), value);
                        return outerFunction.apply(getKey(), inner.setValue(converted));
                    }

                    @Override
                    public String toString() {
                        return String.format("\"%s\": %s", getKey(), getValue());
                    }
                };
            }
        };
    }
}
