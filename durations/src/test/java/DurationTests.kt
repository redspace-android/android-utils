package com.redspace.durations

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
                duration shouldEqual zeroType
            }
        }
    }
})

class DurationTests {
    @Test
    fun `Given two equal durations, when I compare them, then I expect a 0`() {
        // GIVEN
        val a = days(4)
        val b = a.milliseconds.toMilliseconds()

        // WHEN
        val result = a.compareTo(b)

        // THEN
        result shouldEqual 0
    }

    @Test
    fun `Given a small duration, when I compare it to a large duration, then I expect below 0`() {
        // GIVEN
        val large = days(4)
        val small = hours(5)

        // WHEN
        val result = small.compareTo(large)

        // THEN
        result shouldBeLessThan 0
    }

    @Test
    fun `Given a large duration, when I compare it to a small duration, then I expect more than 0`() {
        // GIVEN
        val large = days(4)
        val small = hours(5)

        // WHEN
        val result = large.compareTo(small)

        // THEN
        result shouldBeGreaterThan 0
    }

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