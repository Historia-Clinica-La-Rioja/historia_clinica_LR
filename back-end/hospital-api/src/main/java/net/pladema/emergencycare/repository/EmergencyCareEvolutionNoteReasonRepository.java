package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNoteReason;

import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNoteReasonPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareEvolutionNoteReasonRepository extends JpaRepository<EmergencyCareEvolutionNoteReason, EmergencyCareEvolutionNoteReasonPK> {
}
