package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationArticleRepository extends JpaRepository<CommercialMedicationArticle, Integer> {
}
