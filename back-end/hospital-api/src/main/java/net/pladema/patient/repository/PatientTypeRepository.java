package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.PatientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientTypeRepository extends JpaRepository<PatientType, Short> {
}
