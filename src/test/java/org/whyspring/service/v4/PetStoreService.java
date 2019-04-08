package org.whyspring.service.v4;

import org.whyspring.beans.factory.annotation.Autowired;
import org.whyspring.dao.v4.AccountDao;
import org.whyspring.dao.v4.ItemDao;
import org.whyspring.stereotype.Component;

@Component(value = "petStore")
public class PetStoreService {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }


}