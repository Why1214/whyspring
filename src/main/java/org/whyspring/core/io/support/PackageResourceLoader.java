package org.whyspring.core.io.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.whyspring.core.io.FileSystemResource;
import org.whyspring.core.io.Resource;
import org.whyspring.util.Assert;
import org.whyspring.util.ClassUtils;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class PackageResourceLoader {

    private static final Log logger = LogFactory.getLog(PackageResourceLoader.class);

    private final ClassLoader classLoader;

    public PackageResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PackageResourceLoader(ClassLoader classLoader) {
        Assert.notNull(classLoader, "ResourceLoader must not be null");
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }


    public Resource[] getResources(String basePackage) {
        Assert.notNull(basePackage, "basePackage  must not be null");

        // 将包名转成路径名
        String location = ClassUtils.convertClassNameToResourcePath(basePackage);

        // 将location转成文件对象
        ClassLoader cl = getClassLoader();
        URL url = cl.getResource(location);
        File rootDir = new File(url.getFile());

        Set<File> matchingFiles = retrieveMatchingFiles(rootDir);

        // 生成Resource
        Resource[] resources = new Resource[matchingFiles.size()];

        int i=0;
        for (File file : matchingFiles) {
            resources[i++]=new FileSystemResource(file);
        }
        return resources;
    }

    public Set<File> retrieveMatchingFiles(File rootDir) {
        // 如果此路径不存在，返回空集合
        if (!rootDir.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
            }
            return Collections.emptySet();
        }

        // 如果不是目录，返回空集合
        if (!rootDir.isDirectory()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
            }
            return Collections.emptySet();
        }

        // 如何文件不可读，返回空集合
        if (!rootDir.canRead()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Cannot search for matching files underneath directory [" + rootDir.getAbsolutePath() +
                        "] because the application is not allowed to read the directory");
            }
            return Collections.emptySet();
        }

        Set<File> result = new LinkedHashSet<File>(8);
        doRetrieveMatchingFiles(rootDir, result);
        return result;
    }

    protected void doRetrieveMatchingFiles(File dir, Set<File> result) {
        File[] dirContents = dir.listFiles();

        for (File dirContent : dirContents) {
            // 如果此文件是目录，则递归
            if (dirContent.isDirectory()) {
                if (!dirContent.canRead()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() +
                                "] because the application is not allowed to read the directory");
                    }
                } else {
                    doRetrieveMatchingFiles(dirContent, result);
                }
            } else {
                result.add(dirContent);
            }
        }
    }
}
