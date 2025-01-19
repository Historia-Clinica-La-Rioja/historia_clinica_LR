package net.pladema.booking.repository;

import net.pladema.booking.repository.entity.VProfessionalMedicalCoverage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackofficeProfessionalMedicalCoverageRepository extends JpaRepository<VProfessionalMedicalCoverage, Integer> {
}
