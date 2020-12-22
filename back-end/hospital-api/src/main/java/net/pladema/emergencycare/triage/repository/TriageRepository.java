package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.Triage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageRepository extends JpaRepository<Triage, Integer> {}