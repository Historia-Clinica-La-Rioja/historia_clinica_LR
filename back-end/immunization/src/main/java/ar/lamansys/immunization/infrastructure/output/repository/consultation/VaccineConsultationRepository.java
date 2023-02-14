package ar.lamansys.immunization.infrastructure.output.repository.consultation;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VaccineConsultationRepository extends SGXAuditableEntityJPARepository<VaccineConsultation, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT vc.id " +
			"FROM VaccineConsultation vc " +
			"WHERE vc.patientId IN :patientsIds")
	List<Integer> getVaccineConsultationIdsFromPatients(@Param("patientsIds") List<Integer> patientsIds);

}