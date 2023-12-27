package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportKeeper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolenceReportKeeperRepository extends JpaRepository<ViolenceReportKeeper, Integer> {
}
