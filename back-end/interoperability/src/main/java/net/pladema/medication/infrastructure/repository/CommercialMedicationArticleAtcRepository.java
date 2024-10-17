package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleAtc;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleAtcPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationArticleAtcRepository extends JpaRepository<CommercialMedicationArticleAtc, CommercialMedicationArticleAtcPK> {
}
