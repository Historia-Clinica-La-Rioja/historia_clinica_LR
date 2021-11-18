package ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceHealthConditionRepository extends JpaRepository<ReferenceHealthCondition, ReferenceHealthConditionPk> {
}
