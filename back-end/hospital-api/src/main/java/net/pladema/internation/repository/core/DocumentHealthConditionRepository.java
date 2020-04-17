package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentHealthCondition;
import net.pladema.internation.repository.core.entity.DocumentHealthConditionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentHealthConditionRepository extends JpaRepository<DocumentHealthCondition, DocumentHealthConditionPK> {

}
