package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.PatientAudit;
import net.pladema.patient.repository.entity.PatientAuditPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientAuditRepository extends JpaRepository<PatientAudit, PatientAuditPK> {
}
