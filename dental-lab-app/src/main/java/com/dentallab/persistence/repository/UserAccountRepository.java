package com.dentallab.persistence.repository;

import com.dentallab.persistence.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link UserAccountEntity} persistence.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and query methods
 * for the UserAccountEntity table.
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {

    /**
     * Finds a user by its unique username.
     *
     * @param username the username of the user
     * @return an {@link Optional} containing the user if found, or empty if not
     */
    Optional<UserAccountEntity> findByUsername(String username);
}
