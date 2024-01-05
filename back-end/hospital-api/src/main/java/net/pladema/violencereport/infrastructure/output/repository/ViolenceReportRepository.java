package net.pladema.violencereport.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ViolenceReportRepository extends SGXAuditableEntityJPARepository<ViolenceReport, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT MAX(vr.situationId) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId")
	Integer getPatientLastSituationId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query(" SELECT vr.id " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId " +
			"AND vr.situationId = :situationId")
	List<Integer> getAllReportIdsByPatientIdAndSituationId(@Param("patientId") Integer patientId, @Param("situationId") Short situationId);

}
