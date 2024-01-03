package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ViolenceReportRepository extends JpaRepository<ViolenceReport, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT MAX(vr.situationId) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId")
	Integer getPatientLastSituationId(@Param("patientId") Integer patientId);

}
