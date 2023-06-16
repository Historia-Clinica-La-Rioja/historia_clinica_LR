package net.pladema.clinichistory.documents.infrastructure.output.repository;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VClinicHistoryRepository extends JpaRepository<VClinicHistory, Long> {

	@Query("SELECT vch " +
			"FROM VClinicHistory vch " +
			"WHERE vch.patientId = :patientId " +
			"AND vch.createdOn >= :startDate " +
			"AND vch.createdOn <= :endDate ")
	List<VClinicHistory> getPatientClinicHistory(@Param("patientId") Integer patientId,
												 @Param("startDate") LocalDateTime startDate,
												 @Param("endDate") LocalDateTime endDate);

}
