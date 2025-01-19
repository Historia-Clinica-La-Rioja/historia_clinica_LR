package net.pladema.clinichistory.requests.medicationrequests.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;

@Repository
public interface MedicationRequestRepository extends SGXAuditableEntityJPARepository<MedicationRequest, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT mr.id " +
			"FROM MedicationRequest mr " +
			"WHERE mr.patientId IN :patientsIds")
	List<Integer> getMedicatoinRequestIdsFromPatients(@Param("patientsIds") List<Integer> patientsIds);

	@Transactional(readOnly = true)
	@Query("SELECT mr.id " +
			"FROM MedicationRequest mr " +
			"JOIN Document d ON d.sourceId = mr.id " +
			"WHERE d.id = :documentId")
	Optional<Integer> getIdByDocumentId(@Param("documentId") Long documentId);
	
	@Transactional(readOnly = true)
	@Query("SELECT mr.medicalCoverageId " +
			"FROM MedicationRequest mr " +
			"WHERE mr.id = :id")
	Optional<Integer> getMedicalCoverageId (@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT mr.uuid " +
			"FROM MedicationRequest mr " +
			"WHERE mr.id = :medicationRequestId")
	UUID fetchUUIDById(@Param("medicationRequestId") Integer medicationRequestId);

}
