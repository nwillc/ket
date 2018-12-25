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
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class TryTest {

    @Test
    internal fun testSuccess() {
        val t1 = Try { 10 / 2 }
        assertThat(t1.isSuccess).isTrue()
        assertThat(t1.toString()).isEqualTo("Success: 5")
        assertThat(t1.get()).isEqualTo(5)
    }

    @Test
    internal fun testFailue() {
        @Suppress("DIVISION_BY_ZERO")
        val t1 = Try { 10 / 0 }
        assertThat(t1.isFailure).isTrue()
        assertThat(t1.toString()).isEqualTo("Failure: java.lang.ArithmeticException: / by zero")
        assertThatThrownBy { t1.get() }.isInstanceOf(ArithmeticException::class.java)
    }

    @Test
    internal fun testMapSuccessOnSuccess() {
        val t1 = Try { "HelloWorld" }
        val t2 = t1.map { it.toUpperCase() }
        assertThat(t2.isSuccess).isTrue()
        assertThat(t2.get()).isEqualTo("HELLOWORLD")
    }

    @Test
    internal fun testMapFailureOnSuccess() {
        val t1 = Try { 1 }
        val t2 = t1.map { it / 0 }
        assertThat(t2.isFailure).isTrue()
        assertThat(t2.toString()).startsWith("Failure: java.lang.ArithmeticException:")
    }

    @Test
    internal fun testMapSuccessOnFailure() {
        @Suppress("DIVISION_BY_ZERO")
        val t1 = Try { 10 / 0 }
        val t2 = t1.map { it + 5 }
        assertThat(t2.isFailure).isTrue()
        assertThat(t2.toString()).startsWith("Failure: java.lang.ArithmeticException:")
    }

    @Test
    internal fun testMapFailureOnFailure() {
        @Suppress("DIVISION_BY_ZERO")
        val t1 = Try { 10 / 0 }
        assertThat(t1.isFailure).isTrue()
        @Suppress("DIVISION_BY_ZERO")
        val t2 = t1.map { it / 0 }
        assertThat(t2.isFailure).isTrue()
        assertThat(t2.toString()).startsWith("Failure: java.lang.ArithmeticException:")
    }

    @Test
    internal fun testFlatMapSuccessOnSuccess() {
        val t1 = Try { 2 }
        val t2 = t1.flatMap { i -> Try { i.toString() } }
        assertThat(t2.isSuccess)
        assertThat(t2.get()).isEqualTo("2")
    }

    @Test
    internal fun testFlatMapFailureOnSuccess() {
        val t1 = Try { 2 }
        @Suppress("DIVISION_BY_ZERO")
        val t2 = t1.flatMap { i -> Try { i / 0 } }
        assertThat(t2.isFailure)
        assertThat(t2.toString()).startsWith("Failure: java.lang.ArithmeticException:")
    }

    @Test
    internal fun testFlatMapSuccessOnFailure() {
        @Suppress("DIVISION_BY_ZERO")
        val t1 = Try { 2 / 0 }
        val t2 = t1.flatMap { i -> Try { i.toString() } }
        assertThat(t2.isFailure)
        assertThat(t2.toString()).startsWith("Failure: java.lang.ArithmeticException:")
    }

    @Test
    internal fun testOrElseSuccess() {
        val t1 = Try { 2 }
        assertThat(t1.orElse(Try { 4 })).isEqualTo(t1)
    }

    @Test
    internal fun testOrElseFailure() {
        @Suppress("DIVISION_BY_ZERO")
        val t1 = Try { 2 / 0 }
        val t2 = Try { 4 }
        assertThat(t1.orElse(t2)).isEqualTo(t2)
    }

    @Test
    internal fun testGetOrElseSuccess() {
        val t1 = Try { 2 }
        assertThat(t1.getOrElse(4)).isEqualTo(2)
    }

    @Test
    internal fun testGetOrElseFailure() {
        @Suppress("DIVISION_BY_ZERO")
        val t1 = Try { 2 / 0 }
        assertThat(t1.getOrElse(4)).isEqualTo(4)
    }

    @Test
    internal fun testFoldSuccess() {
        val t1 = Try { 2 }
        val fold = t1.fold({ t -> t.message }, { i -> i * 2 })
        assertThat(fold).isEqualTo(4)
    }

    @Test
    internal fun testFoldFailure() {
        @Suppress("DIVISION_BY_ZERO")
        val t1 = Try { 2 / 0 }
        val fold = t1.fold({ t -> t.message }, { i -> (i * 2).toString() })
        assertThat(fold).isEqualTo("/ by zero")
    }
}