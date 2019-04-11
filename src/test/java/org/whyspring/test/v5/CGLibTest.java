package org.whyspring.test.v5;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.cglib.proxy.*;
import org.whyspring.service.v5.PetStoreService;
import org.whyspring.tx.TransactionManager;

import java.lang.reflect.Method;

public class CGLibTest {

    @Test
    public void testCallBack() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setCallback(new TransactionInterceptor());
        PetStoreService petStoreService = (PetStoreService) enhancer.create();
        petStoreService.placeOrder();

//        petStoreService.toString();
    }

    private static class TransactionInterceptor implements MethodInterceptor {

        TransactionManager tx = new TransactionManager();

        public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            tx.start();
            Object result = methodProxy.invokeSuper(obj, objects);
            tx.commit();
            return result;
        }
    }

    @Test
    public void testFilter() {

        Callback[] callbacks = new Callback[]{new TransactionInterceptor(), NoOp.INSTANCE};

        Class<?>[] types = new Class<?>[callbacks.length];
        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setInterceptDuringConstruction(false);
        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);

        PetStoreService petStore = (PetStoreService) enhancer.create();
        petStore.placeOrder();
//        System.out.println(petStore.toString());
    }

    private static class ProxyCallbackFilter implements CallbackFilter {

        public ProxyCallbackFilter() {
        }

        public int accept(Method method) {
            if (method.getName().startsWith("place")) {
                return 0;
            } else {
                return 1;
            }

        }
    }
}
