package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareStateRepository extends JpaRepository<EmergencyCareState, Short> {}
