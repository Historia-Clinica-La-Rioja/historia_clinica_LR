package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationUpdateFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommercialMedicationUpdateFileRepository extends JpaRepository<CommercialMedicationUpdateFile, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT cmuf.logId " +
			"FROM CommercialMedicationUpdateFile cmuf " +
			"WHERE cmuf.processed = FALSE")
	Long fetchLastNonProcessedLogId();

}
