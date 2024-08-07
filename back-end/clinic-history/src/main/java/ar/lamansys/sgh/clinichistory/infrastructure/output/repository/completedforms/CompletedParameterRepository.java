package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.entity.CompletedParameter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedParameterRepository extends JpaRepository<CompletedParameter, Integer> {
}
