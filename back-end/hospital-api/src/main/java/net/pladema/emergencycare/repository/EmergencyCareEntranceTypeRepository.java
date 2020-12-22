package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.EmergencyCareEntranceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareEntranceTypeRepository extends JpaRepository<EmergencyCareEntranceType, Short> {}
