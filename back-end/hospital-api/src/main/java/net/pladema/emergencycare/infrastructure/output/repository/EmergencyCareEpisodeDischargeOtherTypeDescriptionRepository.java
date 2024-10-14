package net.pladema.emergencycare.infrastructure.output.repository;

import net.pladema.emergencycare.infrastructure.output.entity.EmergencyCareEpisodeDischargeOtherTypeDescription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareEpisodeDischargeOtherTypeDescriptionRepository extends JpaRepository<EmergencyCareEpisodeDischargeOtherTypeDescription, Integer> {
}
