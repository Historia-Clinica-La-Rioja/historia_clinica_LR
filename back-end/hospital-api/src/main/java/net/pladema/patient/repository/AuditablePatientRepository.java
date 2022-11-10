package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.AuditablePatient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditablePatientRepository extends JpaRepository<AuditablePatient, Integer> {
}
