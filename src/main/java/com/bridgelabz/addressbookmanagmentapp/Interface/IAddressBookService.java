package com.bridgelabz.addressbookmanagmentapp.Interface;

import com.bridgelabz.addressbookmanagmentapp.DTO.AddressBookDTO;

import java.util.List;

public interface IAddressBookService {
    List<AddressBookDTO> getAllContacts();
    AddressBookDTO saveContact(AddressBookDTO dto);
}
