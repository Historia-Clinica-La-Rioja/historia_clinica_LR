package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionVerificationStatusRepository extends JpaRepository<ConditionVerificationStatus, String> {

}
