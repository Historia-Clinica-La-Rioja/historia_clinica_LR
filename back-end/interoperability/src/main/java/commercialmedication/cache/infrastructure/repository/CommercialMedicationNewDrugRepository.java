package commercialmedication.cache.infrastructure.repository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationNewDrug;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationNewDrugRepository extends JpaRepository<CommercialMedicationNewDrug, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE CommercialMedicationNewDrug SET description = :description WHERE id = :id ")
	void updateDescription(@Param("id") Integer affectedRecordId, @Param("description") String newEntryDescription);

}
