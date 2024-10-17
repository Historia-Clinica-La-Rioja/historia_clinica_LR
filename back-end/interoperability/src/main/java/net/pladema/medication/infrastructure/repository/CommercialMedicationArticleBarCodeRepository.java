package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleBarCode;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleBarCodePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationArticleBarCodeRepository extends JpaRepository<CommercialMedicationArticleBarCode, CommercialMedicationArticleBarCodePK> {
}
