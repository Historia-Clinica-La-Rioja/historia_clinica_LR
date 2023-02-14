package net.pladema.clinichistory.requests.medicationrequests.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicationRequestRepository extends SGXAuditableEntityJPARepository<MedicationRequest, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT mr.id " +
			"FROM MedicationRequest mr " +
			"WHERE mr.patientId IN :patientsIds")
	List<Integer> getMedicatoinRequestIdsFromPatients(@Param("patientsIds") List<Integer> patientsIds);

}
