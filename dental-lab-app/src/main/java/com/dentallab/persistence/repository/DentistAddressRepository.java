package com.dentallab.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentallab.persistence.entity.DentistAddressEntity;

@Repository
public interface DentistAddressRepository extends JpaRepository<DentistAddressEntity, Long> {
    List<DentistAddressEntity> findByDentistId(Long dentistId);
    List<DentistAddressEntity> findByDentistIdAndActiveTrue(Long dentistId);
    Optional<DentistAddressEntity> findByDentistIdAndPrimaryTrue(Long dentistId);
}

