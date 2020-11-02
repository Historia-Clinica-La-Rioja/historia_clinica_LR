package net.pladema.person.repository;

import net.pladema.patient.repository.entity.MedicalCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalCoverageRepository extends JpaRepository<MedicalCoverage, Integer> {
}
