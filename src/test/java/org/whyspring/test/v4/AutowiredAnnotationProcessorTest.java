package org.whyspring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.whyspring.beans.factory.annotation.AutowiredFieldElement;
import org.whyspring.beans.factory.annotation.InjectionElement;
import org.whyspring.beans.factory.annotation.InjectionMetadata;
import org.whyspring.beans.factory.config.DependencyDescriptor;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.dao.v4.AccountDao;
import org.whyspring.dao.v4.ItemDao;
import org.whyspring.service.v4.PetStoreService;

import java.lang.reflect.Field;
import java.util.List;

public class AutowiredAnnotationProcessorTest {

    AccountDao accountDao = new AccountDao();
    ItemDao itemDao = new ItemDao();
    DefaultBeanFactory beanFactory = new DefaultBeanFactory() {
        public Object resolveDependency(DependencyDescriptor descriptor) {
            if (descriptor.getDependencyType().equals(AccountDao.class)) {
                return accountDao;
            }
            if (descriptor.getDependencyType().equals(ItemDao.class)) {
                return itemDao;
            }
            throw new RuntimeException("can't support types except AccountDao and ItemDao");
        }
    };


    @Test
    public void testGetInjectionMetadata() {

        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        processor.setBeanFactory(beanFactory);

        // 将Class对象转换成InjectionMetadata，封装目标class和其所依赖的属性
        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);
        List<InjectionElement> elements = injectionMetadata.getInjectionElements();
        Assert.assertEquals(2, elements.size());

        assertFieldExists(elements, "accountDao");
        assertFieldExists(elements, "itemDao");

        PetStoreService petStore = new PetStoreService();

        // 给依赖的属性赋值
        injectionMetadata.inject(petStore);

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);

        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);
    }

    private void assertFieldExists(List<InjectionElement> elements, String fieldName) {
        for (InjectionElement ele : elements) {
            AutowiredFieldElement fieldEle = (AutowiredFieldElement) ele;
            Field f = fieldEle.getField();
            if (f.getName().equals(fieldName)) {
                return;
            }
        }
        Assert.fail(fieldName + "does not exist!");
    }
}
