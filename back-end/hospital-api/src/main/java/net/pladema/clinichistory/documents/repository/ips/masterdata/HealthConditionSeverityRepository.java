package net.pladema.clinichistory.documents.repository.ips.masterdata;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.HealthConditionSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthConditionSeverityRepository extends JpaRepository<HealthConditionSeverity, String> {
}
