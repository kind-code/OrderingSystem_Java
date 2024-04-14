package com.order.controller.user;

import com.entity.AddressBook;
import com.order.service.AddressService;
import com.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressController {
    @Autowired
    private AddressService addressService;
    @GetMapping("/list")
    public Result<List<AddressBook>> addressBookList(){
        List<AddressBook> addressBookList = addressService.addressBookList();
        return Result.success(addressBookList);
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddressBook(){
        AddressBook addressBook = addressService.getDefaultAddressBook();
        return Result.success(addressBook);
    }

    @PutMapping()
    public Result updateAddressBook(@RequestBody AddressBook addressBook){
        addressService.updateAddressBook(addressBook);
        return Result.success();
    }
    @DeleteMapping()
    public Result deleteAddressBook(long id){
        addressService.deleteAddressById(id);
      return Result.success();
    }

    @GetMapping("/{id}")
    public Result queryAddress(@PathVariable("id") long id){
        AddressBook addressBook = addressService.queryById(id);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    public Result setDefaultAddress(long id){
        addressService.setDefaultAddress(id);
        return Result.success();
    }



}
