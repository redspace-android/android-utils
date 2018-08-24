package com.redspace.durations

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeLessThan
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.Test
import java.util.concurrent.TimeUnit

object InternedZerosSpec : Spek({
    val cases = mapOf(
            TimeUnit.NANOSECONDS to zero,
            TimeUnit.MICROSECONDS to zero,
            TimeUnit.MILLISECONDS to zero,
            TimeUnit.SECONDS to zero,
            TimeUnit.MINUTES to zero,
            TimeUnit.HOURS to zero,
            TimeUnit.DAYS to zero
    )

    cases.forEach { unit, zeroType ->
        describe("zero constant for the unit $unit") {
            val duration = unit.toDuration(0)
            it("should equal $zeroType") {
                duration shouldBe zeroType
            }
        }
    }
})

object ConstructionMethodSpec : Spek({
    val constructors = listOf(::nanoseconds, ::microseconds, ::milliseconds, ::seconds, ::minutes, ::hours, ::days)
    val ones = constructors.map { it(1) }

    it ("should contain all possible constructors") {
        constructors.size shouldEqual 7
    }

    it ("should have equivalent sizes") {
        constructors.size shouldEqual ones.size
    }

    constructors.forEachIndexed { index, ctor ->
        describe("the result of a specific constructor (constructors[$index])") {
            val one = ctor(1)
            it("should only equal the output of that specific constructor (ones[$index])") {
                one shouldEqual ones[index]
                ones.filterNot { it == one }.size shouldEqual ones.size - 1
            }
        }
    }
})

object ComparableSpec : Spek({

    val constructors = listOf(::nanoseconds, ::microseconds, ::milliseconds, ::seconds, ::minutes, ::hours, ::days)
    val values = constructors.flatMap { ctor -> listOf(1L, 2).map(ctor) }

    values.forEachIndexed { index, value ->
        describe("the duration $value") {
            it ("should be larger than than all values preceeding it") {
                values.subList(0, index).none { it >= value }.shouldBeTrue()
            }
            it ("should be smaller than all values after it") {
                values.subList(index + 1, values.size).none { it <= value }.shouldBeTrue()
            }
        }
    }

    val unsorted = values.shuffled()
    it ("should sort itself correctly") {
        unsorted.sorted() shouldEqual values
    }
})

class DurationTests {
    @Test
    fun `Given a negative duration, when I convert, then I should not crash`() {
        // GIVEN
        val neg = milliseconds(-100)

        // WHEN
        val result = neg.microseconds

        // THEN
        result shouldEqual microseconds(TimeUnit.MILLISECONDS.toMicros(-100))
    }

    @Test
    fun `When I add smaller unit to larger unit, then I do not lose precision`() {
        // GIVEN
        val expected = 1010
        val small = nanoseconds(10)
        val large = microseconds(10)

        // WHEN
        val result = small + large

        // THEN
        result.nanoseconds shouldEqual expected
    }

    @Test
    fun `When I add larger unit to smaller unit, then I do not lose precision`() {
        // GIVEN
        val expected = 1010
        val small = nanoseconds(10)
        val large = microseconds(10)

        // WHEN
        val result = large + small

        // THEN
        result.nanoseconds shouldEqual expected
    }

    @Test
    fun `When I subtract smaller unit from larger unit, then I do not lose precision`() {
        // GIVEN
        val expected = 1010
        val small = nanoseconds(10)
        val large = microseconds(10)

        // WHEN
        val result = small + large

        // THEN
        result.nanoseconds shouldEqual expected
    }

    @Test
    fun `When I subtract larger unit from smaller unit, then I do not lose precision`() {
        // GIVEN
        val expected = 1010
        val small = nanoseconds(10)
        val large = microseconds(10)

        // WHEN
        val result = large + small

        // THEN
        result.nanoseconds shouldEqual expected
    }

    @Test
    fun `When I convert less than 1us to us, then I get 0`() {
        // GIVEN
        val ns = nanoseconds(1)

        // WHEN
        val us = ns.microseconds

        // THEN
        us shouldEqual zero
    }

    @Test
    fun `When I convert less than 1ms to ms, then I get 0`() {
        // GIVEN
        val us = microseconds(1)

        // WHEN
        val ms = us.milliseconds

        // THEN
        ms shouldEqual zero
    }

    @Test
    fun `When I convert less than 1s to s, then I get 0`() {
        // GIVEN
        val ms = milliseconds(1)

        // WHEN
        val s = ms.seconds

        // THEN
        s shouldEqual zero
    }

    @Test
    fun `When I convert less than 1m to m, then I get 0`() {
        // GIVEN
        val s = seconds(1)

        // WHEN
        val m = s.minutes

        // THEN
        m shouldEqual zero
    }

    @Test
    fun `When I convert less than 1h to h, then I get 0`() {
        // GIVEN
        val m = minutes(1)

        // WHEN
        val h = m.hours

        // THEN
        h shouldEqual zero
    }

    @Test
    fun `When I convert less than 1d to d, then I get 0`() {
        // GIVEN
        val h = hours(1)

        // WHEN
        val d = h.days

        // THEN
        d shouldEqual zero
    }

    @Test
    fun `When I compare same duration in different units, then they are equal`() {
        // GIVEN
        val a = milliseconds(100)
        val b = a.nanoseconds.toNanoseconds()

        // WHEN
        val r = a == b

        // THEN
        r.shouldBeTrue()
    }
}