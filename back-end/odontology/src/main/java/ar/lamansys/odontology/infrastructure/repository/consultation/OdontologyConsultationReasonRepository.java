package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OdontologyConsultationReasonRepository extends JpaRepository<OdontologyConsultationReason, OdontologyConsultationReasonPK> {
}
