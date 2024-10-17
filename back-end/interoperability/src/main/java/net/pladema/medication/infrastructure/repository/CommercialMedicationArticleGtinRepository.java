package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleGtin;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleGtinPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationArticleGtinRepository extends JpaRepository<CommercialMedicationArticleGtin, CommercialMedicationArticleGtinPK> {
}
