package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmergencyCareEpisodeDischargeRepository  extends JpaRepository<EmergencyCareDischarge, Integer> {


}
