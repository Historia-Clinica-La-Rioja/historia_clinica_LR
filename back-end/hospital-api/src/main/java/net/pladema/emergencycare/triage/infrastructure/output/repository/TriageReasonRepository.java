package net.pladema.emergencycare.triage.infrastructure.output.repository;

import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReason;

import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReasonPk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageReasonRepository extends JpaRepository<TriageReason, TriageReasonPk> {
}
