package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OdontologyConsultationRepository extends SGXAuditableEntityJPARepository<OdontologyConsultation, Integer> {

    @Query(" SELECT CASE WHEN COUNT(oc) > 0 THEN true ELSE false END " +
           " FROM OdontologyConsultation oc " +
           " WHERE patientId = :patientId ")
    boolean patientHasPreviousConsultations(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT oc.id " +
			"FROM OdontologyConsultation oc " +
			"WHERE oc.patientId IN :patients")
	List<Integer> getOdontologyConsultationIdFromPatients(@Param("patients")List<Integer> patients);

}
