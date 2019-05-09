package ${package}.web.controller;

import static org.testng.Assert.assertTrue;
import mockit.Expectations;
import mockit.Verifications;

import org.testng.annotations.Test;

public class DemoTest {

    @Test
    public void test() {
        new Expectations() {
            {
                // doSomething
            }
        };
        assertTrue(true);

        new Verifications() {
            {
                // verifySomething
            }
        };
    }

}
