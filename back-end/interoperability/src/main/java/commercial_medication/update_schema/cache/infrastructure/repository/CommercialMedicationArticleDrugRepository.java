package commercial_medication.update_schema.cache.infrastructure.repository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationArticleDrug;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationArticleDrugPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationArticleDrugRepository extends JpaRepository<CommercialMedicationArticleDrug, CommercialMedicationArticleDrugPK> {

	@Transactional(readOnly = true)
	@Query("SELECT COUNT(1) " +
			"FROM CommercialMedicationArticleDrug cmad " +
			"WHERE cmad.pk.articleId = :articleId")
	Long existsByArticleId(@Param("articleId") Integer articleId);

	@Transactional
	@Modifying
	@Query("DELETE FROM CommercialMedicationArticleDrug cmad WHERE cmad.pk.articleId = :articleId")
	void deleteByArticleId(@Param("articleId") Integer articleId);
}
