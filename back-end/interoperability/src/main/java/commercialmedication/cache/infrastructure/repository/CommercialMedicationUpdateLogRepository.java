package commercialmedication.cache.infrastructure.repository;

import commercialmedication.cache.domain.CommercialMedicationUpdateLogBo;
import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationUpdateLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationUpdateLogRepository extends JpaRepository<CommercialMedicationUpdateLog, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT COUNT(1) " +
			"FROM CommercialMedicationUpdateLog cmul")
	Long schemaAlreadyInitialized();

	@Transactional(readOnly = true)
	@Query("SELECT NEW commercialmedication.cache.domain.CommercialMedicationUpdateLogBo(id, logId) " +
			"FROM CommercialMedicationUpdateLog cmul " +
			"WHERE cmul.processed = FALSE")
	CommercialMedicationUpdateLogBo fetchLastNonProcessedEntry();

	@Modifying
	@Transactional
	@Query("UPDATE CommercialMedicationUpdateLog SET processed = TRUE WHERE id = :id")
    void setEntryAsProcessed(@Param("id") Integer id);

}
