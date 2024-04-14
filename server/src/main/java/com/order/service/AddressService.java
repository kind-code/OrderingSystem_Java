package com.order.service;

import com.entity.AddressBook;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AddressService {
    List<AddressBook> addressBookList();

    AddressBook getDefaultAddressBook();

    void updateAddressBook(AddressBook addressBook);

    void deleteAddressById(long id);

    AddressBook queryById(long id);

    void setDefaultAddress(long id);
}
