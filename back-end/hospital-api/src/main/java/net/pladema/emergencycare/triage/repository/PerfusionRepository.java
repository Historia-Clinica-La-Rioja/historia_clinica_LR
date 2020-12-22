package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.Perfusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfusionRepository extends JpaRepository<Perfusion, Short> {}