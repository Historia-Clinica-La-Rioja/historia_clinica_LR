package commercialmedication.cache.infrastructure.repository;

import commercialmedication.cache.domain.CommercialMedicationFileUpdateBo;
import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationUpdateFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationUpdateFileRepository extends JpaRepository<CommercialMedicationUpdateFile, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT COUNT(1) " +
			"FROM CommercialMedicationUpdateFile cmuf")
	Long schemaAlreadyInitialized();

	@Transactional(readOnly = true)
	@Query("SELECT NEW commercialmedication.cache.domain.CommercialMedicationFileUpdateBo(id, logId) " +
			"FROM CommercialMedicationUpdateFile cmuf " +
			"WHERE cmuf.processed = FALSE")
	CommercialMedicationFileUpdateBo fetchLastNonProcessedEntry();

	@Modifying
	@Transactional
	@Query("UPDATE CommercialMedicationUpdateFile SET processed = TRUE WHERE id = :id")
    void setEntryAsProcessed(@Param("id") Integer id);

}
