package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OdontologyReasonRepository extends JpaRepository<OdontologyReason, String> {
}
