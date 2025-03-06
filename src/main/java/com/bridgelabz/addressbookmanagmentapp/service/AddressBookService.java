package com.bridgelabz.addressbookmanagmentapp.service;


import com.bridgelabz.addressbookmanagmentapp.DTO.AddressBookDTO;
import com.bridgelabz.addressbookmanagmentapp.Interface.IAddressBookService;
import com.bridgelabz.addressbookmanagmentapp.Repository.AddressRepository;
import com.bridgelabz.addressbookmanagmentapp.model.AddressBookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    private AddressRepository repository;

    @Override
    public List<AddressBookDTO> getAllContacts() {
        return repository.findAll().stream()
                .map(contact -> new AddressBookDTO(contact.getId(), contact.getName(), contact.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public AddressBookDTO saveContact(AddressBookDTO dto) {
        AddressBookModel contact = new AddressBookModel();
        contact.setName(dto.getName());
        contact.setPhone(dto.getPhone());
        AddressBookModel savedContact = repository.save(contact);
        return new AddressBookDTO(savedContact.getId(), savedContact.getName(), savedContact.getPhone());
    }
}