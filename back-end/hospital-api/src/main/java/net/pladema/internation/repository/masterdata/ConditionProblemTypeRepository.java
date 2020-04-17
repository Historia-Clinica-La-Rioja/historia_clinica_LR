package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.ConditionProblemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionProblemTypeRepository extends JpaRepository<ConditionProblemType, String> {

}
