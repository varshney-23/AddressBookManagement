package com.bridgelabz.addressbookmanagmentapp.service;

import com.bridgelabz.addressbookmanagmentapp.DTO.AddressBookDTO;
import com.bridgelabz.addressbookmanagmentapp.Exception.UserException;
import com.bridgelabz.addressbookmanagmentapp.Repository.AddressRepository;
import com.bridgelabz.addressbookmanagmentapp.model.AddressBookModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookServiceTest {

    @Mock
    private AddressRepository repository;

    @InjectMocks
    private AddressBookService service;

    private AddressBookModel contact;
    private AddressBookDTO contactDTO;

    @BeforeEach
    void setUp() {
        contact = new AddressBookModel();
        contact.setId(1L);
        contact.setName("John Doe");
        contact.setPhone("9876543210");

        contactDTO = new AddressBookDTO(1L, "John Doe", "9876543210");
    }

    @Test
    void testGetAllContacts() {
        when(repository.findAll()).thenReturn(Arrays.asList(contact));

        List<AddressBookDTO> contacts = service.getAllContacts();

        assertEquals(1, contacts.size());
        assertEquals("John Doe", contacts.get(0).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllContacts_EmptyList() {
        when(repository.findAll()).thenReturn(Arrays.asList());

        List<AddressBookDTO> contacts = service.getAllContacts();

        assertEquals(0, contacts.size());
    }

    @Test
    void testSaveContact() {
        when(repository.save(any())).thenReturn(contact);

        AddressBookDTO savedContact = service.saveContact(contactDTO);

        assertEquals("John Doe", savedContact.getName());
        assertEquals("9876543210", savedContact.getPhone());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testGetContactById_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(contact));

        AddressBookDTO foundContact = service.getContactById(1L);

        assertEquals("John Doe", foundContact.getName());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetContactById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> service.getContactById(1L));
    }

    @Test
    void testUpdateContact_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(contact));
        when(repository.save(any())).thenReturn(contact);

        AddressBookDTO updatedContact = service.updateContact(1L, new AddressBookDTO(1L, "Jane Doe", "1234567890"));

        assertEquals("Jane Doe", updatedContact.getName());
        assertEquals("1234567890", updatedContact.getPhone());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdateContact_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> service.updateContact(1L, contactDTO));
    }

    @Test
    void testDeleteContact_Found() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean isDeleted = service.deleteContact(1L);

        assertTrue(isDeleted);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteContact_NotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(UserException.class, () -> service.deleteContact(1L));
    }
}
