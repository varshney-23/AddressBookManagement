package com.bridgelabz.addressbookmanagmentapp.Repository;

import com.bridgelabz.addressbookmanagmentapp.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AuthenticationRepository extends JpaRepository<AuthUser, Long> {
    @Query(value = "SELECT * FROM AUTH_USER WHERE EMAIL = :email", nativeQuery = true)
    AuthUser findByEmail(@Param("email") String email);
    AuthUser findByResetToken(String resetToken);
}
