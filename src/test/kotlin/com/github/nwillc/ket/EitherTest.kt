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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EitherTest {
    
    companion object {
        const val leftValue = 123
        val left: Either<Int, String> = Left(leftValue)
        const val rightValue = "Hello"
        val right: Either<Int, String> = Right(rightValue)
    }

    @Test
    internal fun testLeft() {
        assertThat(left).isInstanceOf(Either::class.java)
        assertThat(left).isInstanceOf(Left::class.java)
        assertThat(left.toString()).isEqualTo("Left: $leftValue")
        assertThat(left.isLeft).isTrue()
        assertThat(left.isRight).isFalse()
        assertThat((left as Left).value).isEqualTo(123)
    }

    @Test
    internal fun testRight() {
        assertThat(right).isInstanceOf(Either::class.java)
        assertThat(right).isInstanceOf(Right::class.java)
        assertThat(right.toString()).isEqualTo("Right: $rightValue")
        assertThat((right as Right).value).isEqualTo(rightValue)
    }

    @Test
    internal fun testFold() {
        assertThat(left.fold({ it + 1 }, { it.toLowerCase() } )).isEqualTo(leftValue + 1)
        assertThat(right.fold({ it + 1 }, { it.toLowerCase() } )).isEqualTo(rightValue.toLowerCase())
    }

    @Test
    internal fun testGetLeft() {
        assertThat(left.getLeft()).isEqualTo(leftValue)
    }

    @Test
    internal fun testGetRight() {
        assertThat(right.getRight()).isEqualTo(rightValue)
    }

    @Test
    internal fun testGetLeftOnRight() {
        assertThat(right.getLeft()).isNull()
    }

    @Test
    internal fun testGetRightOnLeft() {
        assertThat(left.getRight()).isNull()
    }

    @Test
    internal fun testMapLeft() {
        assertThat(left.mapLeft { it * 2 } ).isEqualTo(leftValue * 2)
    }

    @Test
    internal fun testMapRight() {
        assertThat(right.mapRight { it.toUpperCase() } ).isEqualTo(rightValue.toUpperCase())
    }

    @Test
    internal fun testRightMapLeft() {
        assertThat(right.mapLeft { it * 2 } ).isNull()
    }

    @Test
    internal fun testLeftMapRight() {
        assertThat(left.mapRight { it.toUpperCase() } ).isNull()
    }
}
