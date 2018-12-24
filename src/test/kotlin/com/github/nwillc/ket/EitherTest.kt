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

import com.github.nwillc.ket.Either.Left
import com.github.nwillc.ket.Either.Right
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EitherTest {
    @Test
    internal fun testLeft() {
        val either: Either<Int, String> = Left(123)

        assertThat(either).isInstanceOf(Either::class.java)
        assertThat(either).isInstanceOf(Left::class.java)
        assertThat(either.toString()).isEqualTo("Left: 123")
        assertThat(either.isLeft).isTrue()
        assertThat(either.isRight).isFalse()
        assertThat((either as Left).value).isEqualTo(123)
    }

    @Test
    internal fun testRight() {
        val either: Either<Int, String> = Right("Hello")

        assertThat(either).isInstanceOf(Either::class.java)
        assertThat(either).isInstanceOf(Right::class.java)
        assertThat(either.toString()).isEqualTo("Right: Hello")
        assertThat((either as Right).value).isEqualTo("Hello")
    }

    @Test
    internal fun testFold() {
        val either1: Either<Int, String> = Left(123)
        assertThat(either1.fold({ it + 1 }, { it.toLowerCase() })).isEqualTo(124)
        val either2: Either<Int, String> = Right("HELLO")
        assertThat(either2.fold({ it + 1 }, { it.toLowerCase() })).isEqualTo("hello")
    }
}
