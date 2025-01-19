package net.pladema.emergencycare.triage.infrastructure.output.repository;

import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReason;

import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReasonPk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriageReasonRepository extends JpaRepository<TriageReason, TriageReasonPk> {

	List<TriageReason> findAllByPkTriageId(Integer triageId);
}
