package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleDrug;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleDrugPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationArticleDrugRepository extends JpaRepository<CommercialMedicationArticleDrug, CommercialMedicationArticleDrugPK> {
}
