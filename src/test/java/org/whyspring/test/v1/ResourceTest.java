package org.whyspring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.core.io.FileSystemResource;
import org.whyspring.core.io.Resource;

import java.io.InputStream;

public class ResourceTest {

    @Test
    public void testClassPathResource() throws Exception {

        Resource r = new ClassPathResource("petstore-v1.xml");

        InputStream is = null;

        try {
            is = r.getInputStream();
            // 注意：这个测试其实并不充分！！
            Assert.assertNotNull(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    @Test
    public void testFileSystemResource() throws Exception {

		Resource r = new FileSystemResource("/install/code/whyspring/src/test/resources/petstore-v1.xml");
		InputStream is = null;
		try {
			is = r.getInputStream();
			// 注意：这个测试其实并不充分！！
			Assert.assertNotNull(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
    }
}
