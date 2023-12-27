package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportAggressor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolenceReportAggressorRepository extends JpaRepository<ViolenceReportAggressor, Integer> {
}
