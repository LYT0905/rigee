package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.AddressBookMapper;
import com.pojo.AddressBook;
import com.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2024/2/5
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
