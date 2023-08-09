package ar.lamansys.virtualConsultation.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultationClinicalProfessionalAvailability;

@Repository
public interface ClinicalProfessionalAvailabilityRepository extends JpaRepository<VirtualConsultationClinicalProfessionalAvailability, Integer> {
}
