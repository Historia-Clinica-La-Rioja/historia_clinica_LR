package net.pladema.clinichistory.documents.repository.ips.masterdata;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionClinicalStatusRepository extends JpaRepository<ConditionClinicalStatus, String> {

}
