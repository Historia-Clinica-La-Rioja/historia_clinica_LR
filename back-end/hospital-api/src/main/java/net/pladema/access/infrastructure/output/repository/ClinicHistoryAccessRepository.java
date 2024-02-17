package net.pladema.access.infrastructure.output.repository;

import net.pladema.access.infrastructure.output.repository.entity.ClinicHistoryAudit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicHistoryAccessRepository extends JpaRepository<ClinicHistoryAudit, Integer> {
}
