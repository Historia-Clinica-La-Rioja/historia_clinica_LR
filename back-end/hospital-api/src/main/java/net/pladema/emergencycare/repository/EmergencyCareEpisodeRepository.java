package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareEpisodeRepository extends JpaRepository<EmergencyCareEpisode, Integer> {}
