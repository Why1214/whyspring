package org.whyspring.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.whyspring.test.v1.V1AllTests;
import org.whyspring.test.v2.V2AllTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        V1AllTests.class,
        V2AllTests.class
})
public class AllTests {
}
