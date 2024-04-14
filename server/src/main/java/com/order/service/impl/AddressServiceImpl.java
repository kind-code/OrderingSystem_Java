package com.order.service.impl;

import com.context.BaseContext;
import com.entity.AddressBook;
import com.order.mapper.AddressMapper;
import com.order.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {


    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AddressBook queryById(long id) {
      AddressBook addressBook = addressMapper.queryById(id);
        return null;
    }

    @Override
    public void setDefaultAddress(long id) {
        addressMapper.setdefaultAddress(id);
    }

    @Override
    public List<AddressBook> addressBookList() {
        //获取用户id
        Long id = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        //根据ID查询IP地址
        List<AddressBook> addressBookList = addressMapper.queryByUserId(id);
        return addressBookList;
    }

    @Override
    public AddressBook getDefaultAddressBook() {
        //获取用户id
        Long id = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        AddressBook addressBook = addressMapper.queryDefaultAddressBook(id);
        return addressBook;
    }

    @Override
    public void updateAddressBook(AddressBook addressBook) {
        addressMapper.updateAddressBook(addressBook);
    }

    @Override
    public void deleteAddressById(long id) {
        addressMapper.deleteAddressById(id);
    }
}
