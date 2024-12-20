package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIsolationAlert;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIsolationAlertPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentIsolationAlertRepository extends JpaRepository<DocumentIsolationAlert, DocumentIsolationAlertPK> {
    Optional<DocumentIsolationAlert> findByPk_isolationAlertId(Integer alertId);
}
