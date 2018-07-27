import org.junit.Test

class ComponentsTests {

    val testSubject = ComponentManager<Unit>()

    @Test
    fun `Given I am waiting for a component, when it becomes available, then I can execute my work`() {
        // GIVEN
        val obs = testSubject.component.test()

        // WHEN
        testSubject.consume(Unit)

        // THEN
        obs.assertValueCount(1).assertComplete()
    }

    @Test
    fun `Given I am waiting for a component, if it never becomes available, then I never execute my work`() {
        // GIVEN
        val obs = testSubject.component.test()

        // THEN
        obs.assertNoValues().assertNotComplete().assertNoErrors()
    }

    @Test
    fun `Given I am waiting for a component, if I clear, then I never execute my work`() {
        // GIVEN
        val obs = testSubject.component.test()

        // WHEN
        testSubject.clear()

        // THEN
        obs.assertNoValues().assertNotComplete().assertNoErrors()
    }

    @Test
    fun `Given I have a component and clear it, when I start listening, then I never execute my work`() {
        // GIVEN
        testSubject.consume(Unit)
        testSubject.clear()

        // WHEN
        val obs = testSubject.component.test()

        // THEN
        obs.assertNoValues().assertNotComplete().assertNoErrors()
    }

    @Test
    fun `Given I have component A, when I set component B and listen, then I get component B`() {
        // GIVEN
        val testSubject = ComponentManager<String>()
        testSubject.consume("A")

        // WHEN
        testSubject.consume("B")
        val obs = testSubject.component.test()

        // THEN
        obs.assertComplete().assertValueCount(1).assertValueAt(0, { it == "B" })
    }
}