package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OdontologyConsultationRepository extends SGXAuditableEntityJPARepository<OdontologyConsultation, Integer> {

    @Query(" SELECT CASE WHEN COUNT(oc) > 0 THEN true ELSE false END " +
           " FROM OdontologyConsultation oc " +
           " WHERE patientId = :patientId ")
    boolean patientHasPreviousConsultations(@Param("patientId") Integer patientId);

}
