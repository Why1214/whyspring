package org.whyspring.context.support;

import org.whyspring.core.io.ClassPathResource;
import org.whyspring.core.io.Resource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    public ClassPathXmlApplicationContext(String configFile) {
        super(configFile);
    }

    public Resource getResourceByPath(String configFile) {
        return new ClassPathResource(configFile, this.getBeanClassLoader());
    }


}
