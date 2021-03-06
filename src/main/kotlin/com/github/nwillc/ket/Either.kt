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
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 * ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ket

/**
 * An Either implementation. An Either is a class tasked with container either one or another type.
 * It has two types, a left or a right, and is either one or the other.
 */
@Suppress("unused")
sealed class Either<out L, out R> {
    /**
     * Is this a right side of the Either?
     */
    val isRight get() = this is Right<R>
    /**
     * Is this a left side of the Either?
     */
    val isLeft get() = this is Left<L>
    /**
     * Fold the either with one of two functions based on if it is Left or Right.
     */
    abstract fun <T> fold(lfn: (L) -> T, rfn: (R) -> T): T
    /**
     * Get the left value of the Either, or null if the Either is a Right.
     */
    abstract fun getLeft(): L?
    /**
     * Get the right value of the Either, or null if the Either is a Left.
     */
    abstract fun getRight(): R?
    /**
     * Apply a mapping function to the Either if it is a Left.
     */
    abstract fun <T> mapLeft(fn: (L) -> T): T?
    /**
     * Apply a mapping function to the Either if it is a Right.
     */
    abstract fun <T> mapRight(fn: (R) -> T): T?
}

class Left<out L>(val value: L) : Either<L, Nothing>() {
    override fun <T> fold(lfn: (L) -> T, rfn: (Nothing) -> T): T = lfn(value)
    override fun getLeft(): L? = value
    override fun getRight(): Nothing? = null
    override fun <T> mapLeft(fn: (L) -> T): T? = fn(value)
    override fun <T> mapRight(fn: (Nothing) -> T): T? = null
    override fun toString(): String = "Left: $value"
}

class Right<out R>(val value: R) : Either<Nothing, R>() {
    override fun <T> fold(lfn: (Nothing) -> T, rfn: (R) -> T): T = rfn(value)
    override fun getLeft(): Nothing? = null
    override fun getRight(): R? = value
    override fun <T> mapLeft(fn: (Nothing) -> T): T? = null
    override fun <T> mapRight(fn: (R) -> T): T? = fn(value)
    override fun toString(): String = "Right: $value"
}
