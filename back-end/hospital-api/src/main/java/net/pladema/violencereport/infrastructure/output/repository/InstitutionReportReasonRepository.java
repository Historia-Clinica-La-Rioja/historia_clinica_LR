package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportReasonPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.InstitutionReportReason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionReportReasonRepository extends JpaRepository<InstitutionReportReason, InstitutionReportReasonPK> {
}
