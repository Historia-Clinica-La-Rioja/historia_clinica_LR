package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.EmergencyCareType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareTypeRepository extends JpaRepository<EmergencyCareType, Short> {}
