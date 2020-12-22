package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.EmergencyCareEpisodeReason;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisodeReasonPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareEpisodeReasonRepository extends JpaRepository<EmergencyCareEpisodeReason, EmergencyCareEpisodeReasonPK> {}
