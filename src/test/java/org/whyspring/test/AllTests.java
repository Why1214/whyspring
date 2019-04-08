package org.whyspring.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.whyspring.test.v1.V1AllTests;
import org.whyspring.test.v2.V2AllTests;
import org.whyspring.test.v3.V3AllTests;
import org.whyspring.test.v4.V4AllTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        V1AllTests.class,
        V2AllTests.class,
        V3AllTests.class,
        V4AllTests.class
})
public class AllTests {
}
