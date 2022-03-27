package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.TriageRiskFactors;
import net.pladema.emergencycare.triage.repository.entity.TriageRiskFactorsPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageRiskFactorsRepository extends JpaRepository<TriageRiskFactors, TriageRiskFactorsPk> {

}
