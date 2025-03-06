package com.bridgelabz.addressbookmanagmentapp.controller;


import com.bridgelabz.addressbookmanagmentapp.DTO.AddressBookDTO;
import com.bridgelabz.addressbookmanagmentapp.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class ContactController {

    @Autowired
    AddressBookService addressBookService;

    @GetMapping("/show")
    public ResponseEntity<List<AddressBookDTO>> getAllContacts() {
        return ResponseEntity.ok(addressBookService.getAllContacts());
    }
    @PostMapping("/add")
    public ResponseEntity<AddressBookDTO> addContact(@RequestBody AddressBookDTO dto) {
        return ResponseEntity.ok(addressBookService.saveContact(dto));
    }
}
