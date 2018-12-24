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

@Suppress("unused")
sealed class Either<out L, out R> {
    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <T> fold(lfn: (L) -> T, rfn: (R) -> T): T =
        when (this) {
            is Left -> lfn(value)
            is Right -> rfn(value)
        }

    class Left<out L>(val value: L) : Either<L, Nothing>() {
        override fun toString(): String = "Left: $value"
    }

    class Right<out R>(val value: R) : Either<Nothing, R>() {
        override fun toString(): String = "Right: $value"
    }
}