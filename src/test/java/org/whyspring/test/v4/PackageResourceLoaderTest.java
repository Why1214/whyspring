package org.whyspring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.core.io.Resource;
import org.whyspring.core.io.support.PackageResourceLoader;

public class PackageResourceLoaderTest {

    @Test
    public void testGetResources() {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("org.whyspring.dao.v4");
        Assert.assertEquals(2, resources.length);
    }
}
