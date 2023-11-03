package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutpatientConsultationRepository extends SGXAuditableEntityJPARepository<OutpatientConsultation, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT oc.id " +
			"FROM OutpatientConsultation AS oc " +
			"WHERE oc.patientId IN :patientIds")
	List<Integer> getOutpatientConsultationIdsFromPatients(@Param("patientIds") List<Integer> patientIds);

	@Transactional(readOnly = true)
	@Query("SELECT oc.patientMedicalCoverageId " +
			"FROM OutpatientConsultation oc " +
			"WHERE oc.id = :id")
	Optional<Integer> getPatientMedicalCoverageId (@Param("id") Integer id);

}