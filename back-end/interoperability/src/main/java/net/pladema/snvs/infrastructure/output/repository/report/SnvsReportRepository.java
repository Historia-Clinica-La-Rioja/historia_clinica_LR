package net.pladema.snvs.infrastructure.output.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnvsReportRepository extends JpaRepository<SnvsReport, Integer> {
}
