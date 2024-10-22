package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommercialMedicationArticleRepository extends JpaRepository<CommercialMedicationArticle, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE CommercialMedicationArticle SET presentationStatus = 'A' WHERE id IN :articleIds")
	void reEnableAll(@Param("articleIds") List<Integer> articleIds);

}
