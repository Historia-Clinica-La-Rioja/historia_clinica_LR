package net.pladema.access.infrastructure.output.repository;

import net.pladema.access.infrastructure.output.repository.entity.ClinicHistoryAudit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicHistoryAccessRepository extends JpaRepository<ClinicHistoryAudit, Integer> {

	List<ClinicHistoryAudit> findByPatientIdAndUserIdOrderByAccessDateDesc(Integer patientId, Integer userId);
}
