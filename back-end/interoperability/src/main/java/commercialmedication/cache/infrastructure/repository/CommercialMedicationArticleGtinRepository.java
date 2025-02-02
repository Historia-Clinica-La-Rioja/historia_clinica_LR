package commercialmedication.cache.infrastructure.repository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleGtin;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleGtinPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationArticleGtinRepository extends JpaRepository<CommercialMedicationArticleGtin, CommercialMedicationArticleGtinPK> {

	@Transactional(readOnly = true)
	@Query("SELECT COUNT(1) " +
			"FROM CommercialMedicationArticleGtin cmag " +
			"WHERE cmag.pk.articleId = :articleId ")
	Long existsByArticleId(@Param("articleId") Integer articleId);

	@Transactional
	@Modifying
	@Query("DELETE FROM CommercialMedicationArticleGtin cmag WHERE cmag.pk.articleId = :articleId")
	void deleteByArticleId(@Param("articleId") Integer articleId);
}
