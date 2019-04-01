package org.whyspring.context.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.BeanDefinitionStoreException;
import org.whyspring.beans.factory.support.BeanDefinitionRegistry;
import org.whyspring.beans.factory.support.BeanNameGenerator;
import org.whyspring.core.io.Resource;
import org.whyspring.core.io.support.PackageResourceLoader;
import org.whyspring.core.type.classreading.MetadataReader;
import org.whyspring.core.type.classreading.SimpleMetadataReader;
import org.whyspring.stereotype.Component;
import org.whyspring.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    private PackageResourceLoader resourceLoader = new PackageResourceLoader();

    protected final Log logger = LogFactory.getLog(getClass());

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinition> doScan(String packagesToScan) {
        // 将字符串转换成包数组
        String[] basePackages = StringUtils.tokenizeToStringArray(packagesToScan, ",");

        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<BeanDefinition>();
        for (String basePackage : basePackages) {
            // 将带注解的类转换成beanDefinition的集合
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                beanDefinitions.add(candidate);
                // 将beanDefinition注册到总的集合中
                registry.registerBeanDefinition(candidate.getBeanId(), candidate);

            }
        }
        return beanDefinitions;
    }

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {

        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        try {
            // 通过PackageResourceLoader将文件转成resource集合
            Resource[] resources = this.resourceLoader.getResources(basePackage);
            for (Resource resource : resources) {
                try {
                    // 将resource中的类进行visitor，并封装到MetadataReader中
                    MetadataReader metadataReader = new SimpleMetadataReader(resource);
                    // 如果存在Component注解，则生成BeanDefinition
                    if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                        // 实例化一个BeanDefinition
                        ScannedGenericBeanDefinition sbd =
                                new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                        // 获取beanId
                        String beanName = this.beanNameGenerator.generateBeanName(sbd, this.registry);
                        sbd.setBeanId(beanName);
                        candidates.add(sbd);
                    }
                } catch (Throwable ex) {
                    throw new BeanDefinitionStoreException(
                            "Failed to read candidate component class: " + resource, ex);
                }

            }
        } catch (Exception ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }
}
