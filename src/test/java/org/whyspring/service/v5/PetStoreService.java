package org.whyspring.service.v5;

import org.whyspring.beans.factory.annotation.Autowired;
import org.whyspring.dao.v5.AccountDao;
import org.whyspring.dao.v5.ItemDao;
import org.whyspring.stereotype.Component;
import org.whyspring.util.MessageTracker;

@Component(value = "petStore")
public class PetStoreService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    ItemDao itemDao;

    public PetStoreService() {

    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void placeOrder() {
        System.out.println("place order");
        MessageTracker.addMsg("place order");
    }
}