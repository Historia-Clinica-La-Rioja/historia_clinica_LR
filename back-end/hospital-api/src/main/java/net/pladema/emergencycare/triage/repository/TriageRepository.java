package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.Triage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriageRepository extends JpaRepository<Triage, Integer>, TriageRepositoryCustom {
}