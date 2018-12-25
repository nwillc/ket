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
    abstract val either: Either<Throwable, T>

    fun <U> fold(fa: (Throwable) -> U, fb: (T) -> U): U = either.fold(fa, fb)
    abstract fun get(): T
    abstract fun getOrElse(default: @UnsafeVariance T): T
    abstract fun orElse(default: Try<@UnsafeVariance T>): Try<T>
    abstract fun <U> map(f: (T) -> U): Try<U>
    abstract fun <U> flatMap(f: (T) -> Try<U>): Try<U>
}

class Success<out T>(value: T) : Try<T>() {
    override val either = Either.Right(value)
    override fun get(): T = either.value
    override fun toString() = "Success: ${either.value}"
    override fun <U> map(f: (T) -> U): Try<U> = Try { f(either.value) }
    override fun <U> flatMap(f: (T) -> Try<U>): Try<U> = f(either.value)
    override fun getOrElse(default: @UnsafeVariance T): T = either.value
    override fun orElse(default: Try<@UnsafeVariance T>): Try<T> = this
}

class Failure<out T>(e: Throwable) : Try<T>() {
    override val either = Either.Left(e)
    override fun get(): T = throw either.value
    override fun toString() = "Failure: ${either.value}"
    @Suppress("UNCHECKED_CAST")
    override fun <U> map(f: (T) -> U): Try<U> = this as Try<U>
    @Suppress("UNCHECKED_CAST")
    override fun <U> flatMap(f: (T) -> Try<U>): Try<U> = this as Try<U>
    override fun getOrElse(default: @UnsafeVariance T): T = default
    override fun orElse(default: Try<@UnsafeVariance T>): Try<T> = default
}
