package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationAction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationActionRepository extends JpaRepository<CommercialMedicationAction, Integer> {
}
