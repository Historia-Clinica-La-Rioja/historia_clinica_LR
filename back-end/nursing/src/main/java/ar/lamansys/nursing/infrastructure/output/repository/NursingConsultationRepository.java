package ar.lamansys.nursing.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NursingConsultationRepository extends SGXAuditableEntityJPARepository<NursingConsultation, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT nc.id " +
			"FROM NursingConsultation nc " +
			"WHERE nc.patientId IN :patientIds")
	List<Integer> getNursingConsultationIdsFromPatients(@Param("patientIds") List<Integer> patientIds);
}

