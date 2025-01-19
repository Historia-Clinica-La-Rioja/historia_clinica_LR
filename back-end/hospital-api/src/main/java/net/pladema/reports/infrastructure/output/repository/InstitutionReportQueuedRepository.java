package net.pladema.reports.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.reports.infrastructure.output.repository.entity.InstitutionReportQueued;

@Repository
public interface InstitutionReportQueuedRepository extends JpaRepository<InstitutionReportQueued, Integer> {


}
