package net.pladema.audit.repository;

import net.pladema.audit.repository.entity.ViewClinicHistoryAudit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewClinicHistoryAuditRepository extends JpaRepository<ViewClinicHistoryAudit, Integer> {

}
