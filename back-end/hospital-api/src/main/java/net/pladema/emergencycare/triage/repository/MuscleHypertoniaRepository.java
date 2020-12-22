package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.MuscleHypertonia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuscleHypertoniaRepository extends JpaRepository<MuscleHypertonia, Short> {}
