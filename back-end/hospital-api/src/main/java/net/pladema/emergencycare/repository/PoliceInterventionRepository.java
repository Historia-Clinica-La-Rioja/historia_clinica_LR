package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.PoliceInterventionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliceInterventionRepository extends JpaRepository<PoliceInterventionDetails, Integer> {}