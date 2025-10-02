package com.dentallab.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentallab.persistence.entity.DentistEmailEntity;

@Repository
public interface DentistEmailRepository extends JpaRepository<DentistEmailEntity, Long> {
    List<DentistEmailEntity> findByDentistId(Long dentistId);
    List<DentistEmailEntity> findByDentistIdAndActiveTrue(Long dentistId);
    Optional<DentistEmailEntity> findByDentistIdAndPrimaryTrue(Long dentistId);
}
