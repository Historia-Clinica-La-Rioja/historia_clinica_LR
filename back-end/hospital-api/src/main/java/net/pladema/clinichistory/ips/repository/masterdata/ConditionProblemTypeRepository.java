package net.pladema.clinichistory.ips.repository.masterdata;

import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionProblemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionProblemTypeRepository extends JpaRepository<ConditionProblemType, String> {

}
