/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yegor256.rpm;

import io.reactivex.rxjava3.core.Completable;
import java.nio.file.Path;

/**
 * Synchronous act wrapper.
 *
 * @since 0.1
 */
class SynchronousAct implements Repomd.Act {

    /**
     * The lock to synchronize on.
     */
    private final ReactiveLock lock;

    /**
     * Wrapped act.
     */
    private final Repomd.Act act;

    /**
     * Create an act with synchronization on a lock.
     *
     * @param act Act to synchronize.
     * @param lock The lock to sync on.
     */
    SynchronousAct(final Repomd.Act act, final ReactiveLock lock) {
        this.act = act;
        this.lock = lock;
    }

    @Override
    public Completable update(final Path file) {
        return this.lock.lock()
            .andThen(this.act.update(file))
            .doOnTerminate(this.lock::unlock);
    }
}
