/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without
 * fee is hereby granted, provided that the above copyright notice and this permission notice appear
 * in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE
 * LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 *  FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 *  ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ket

sealed class Try<out T> {
    companion object {
        operator fun <T> invoke(body: () -> T): Try<T> = try {
            Success(body())
        } catch (e: Throwable) {
            Failure(e)
        }
    }

    val isSuccess get() = either.isRight
    val isFailure get() = either.isLeft
    protected abstract val either: Either<Throwable, T>

    fun <U> fold(ff: (Throwable) -> U, fs: (T) -> U): U = either.fold(ff, fs)
    abstract fun <U> flatMap(fn: (T) -> Try<U>): Try<U>
    abstract fun <U> map(fn: (T) -> U): Try<U>
    abstract fun get(): T
    abstract fun getOrElse(default: @UnsafeVariance T): T
    abstract fun orElse(default: Try<@UnsafeVariance T>): Try<T>
}

class Success<out T>(value: T) : Try<T>() {
    override val either = Right(value)
    override fun <U> flatMap(fn: (T) -> Try<U>): Try<U> = fn(either.value)
    override fun <U> map(fn: (T) -> U): Try<U> = Try { fn(either.value) }
    override fun get(): T = either.value
    override fun getOrElse(default: @UnsafeVariance T): T = either.value
    override fun orElse(default: Try<@UnsafeVariance T>): Try<T> = this
    override fun toString() = "Success: ${either.value}"
}

class Failure<out T>(e: Throwable) : Try<T>() {
    override val either = Left(e)
    @Suppress("UNCHECKED_CAST")
    override fun <U> flatMap(fn: (T) -> Try<U>): Try<U> = this as Try<U>
    @Suppress("UNCHECKED_CAST")
    override fun <U> map(fn: (T) -> U): Try<U> = this as Try<U>
    override fun get(): T = throw either.value
    override fun getOrElse(default: @UnsafeVariance T): T = default
    override fun orElse(default: Try<@UnsafeVariance T>): Try<T> = default
    override fun toString() = "Failure: ${either.value}"
}
