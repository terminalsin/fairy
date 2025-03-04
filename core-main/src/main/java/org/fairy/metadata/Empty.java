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

package org.fairy.metadata;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * An object which represents nothing.
 *
 * <p>Used mostly by {@link MetadataKey}s, where the presence of the key in the map
 * is enough for a behaviour to apply. In other words, the value is not significant.</p>
 *
 * <p>Very similar to {@link Void}, except this class also provides an instance of the "empty" object.</p>
 */
public final class Empty {
    private static final Empty INSTANCE = new Empty();
    private static final Supplier<Empty> SUPPLIER = () -> INSTANCE;

    @Nonnull
    public static Empty instance() {
        return INSTANCE;
    }

    @Nonnull
    public static Supplier<Empty> supplier() {
        return SUPPLIER;
    }

    private Empty() {

    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public String toString() {
        return "Empty";
    }
}
