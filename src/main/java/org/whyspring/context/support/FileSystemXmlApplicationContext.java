package org.whyspring.context.support;

import org.whyspring.core.io.FileSystemResource;
import org.whyspring.core.io.Resource;

public class FileSystemXmlApplicationContext extends AbstractApplicationContext {

    public FileSystemXmlApplicationContext(String configFile){
        super(configFile);
    }

    public Resource getResourceByPath(String configFile) {
        return new FileSystemResource(configFile);
    }
}
