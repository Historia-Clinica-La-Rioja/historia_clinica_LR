package net.pladema.clinichistory.documents.repository.ips;

import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticReportRepository extends JpaRepository<DiagnosticReport, Integer> {
}
