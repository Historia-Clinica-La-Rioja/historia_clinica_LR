package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.PatientMedicalCoverageAssn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientMedicalCoverageRepository extends JpaRepository<PatientMedicalCoverageAssn, Integer>{}
