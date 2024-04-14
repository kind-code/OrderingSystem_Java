package com.order.mapper;

import com.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressMapper {
    @Select("select * from address_book where user_id=#{id}")
    List<AddressBook> queryByUserId(Long id);
    @Select("select * from address_book where user_id=#{id} and is_default = 1")
    AddressBook queryDefaultAddressBook(Long id);

    void updateAddressBook(AddressBook addressBook);
    @Delete("delete from address_book where id = #{id}")
    void deleteAddressById(long id);
    @Select("select * from address_book where id = #{id}")
    AddressBook queryById(long id);

    @Update("update address_book set is_default=1 where id = #{id}")
    void setdefaultAddress(long id);
}
