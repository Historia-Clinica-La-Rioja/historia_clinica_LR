package commercial_medication.update_schema.cache.infrastructure.repository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationSize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationSizeRepository extends JpaRepository<CommercialMedicationSize, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE CommercialMedicationSize SET description = :description WHERE id = :id ")
	void updateDescription(@Param("id") Integer affectedRecordId, @Param("description") String newEntryDescription);

}
