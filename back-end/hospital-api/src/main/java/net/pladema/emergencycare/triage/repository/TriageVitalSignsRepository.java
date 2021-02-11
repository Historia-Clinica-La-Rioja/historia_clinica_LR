package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.TriageVitalSigns;
import net.pladema.emergencycare.triage.repository.entity.TriageVitalSignsPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageVitalSignsRepository extends JpaRepository<TriageVitalSigns, TriageVitalSignsPk> {

}
