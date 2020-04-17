package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.ConditionClinicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionClinicalStatusRepository extends JpaRepository<ConditionClinicalStatus, String> {

}
