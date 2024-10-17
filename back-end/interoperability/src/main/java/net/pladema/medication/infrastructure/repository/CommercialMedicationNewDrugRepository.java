package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationNewDrug;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationNewDrugRepository extends JpaRepository<CommercialMedicationNewDrug, Integer> {
}
