package com.bridgelabz.addressbookmanagmentapp.Repository;

import com.bridgelabz.addressbookmanagmentapp.model.AddressBookModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressBookModel,Long> {
}
