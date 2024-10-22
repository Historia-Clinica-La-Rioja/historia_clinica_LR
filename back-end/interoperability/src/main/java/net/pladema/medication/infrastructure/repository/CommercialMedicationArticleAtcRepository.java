package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleAtc;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleAtcPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationArticleAtcRepository extends JpaRepository<CommercialMedicationArticleAtc, CommercialMedicationArticleAtcPK> {

	@Transactional
	@Modifying
	@Query("DELETE FROM CommercialMedicationArticleAtc cmaa WHERE cmaa.pk.articleId = :articleId")
	void deleteByArticleId(@Param("articleId") Integer articleId);

	@Transactional(readOnly = true)
	@Query("SELECT COUNT(1) " +
			"FROM CommercialMedicationArticleAtc cmaa " +
			"WHERE cmaa.pk.articleId = :articleId")
	Long existsByArticleId(@Param("articleId") Integer articleId);
}
