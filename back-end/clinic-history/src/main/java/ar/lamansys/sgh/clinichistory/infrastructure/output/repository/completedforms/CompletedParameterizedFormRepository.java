package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.entity.CompletedParameterizedForm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedParameterizedFormRepository extends JpaRepository<CompletedParameterizedForm, Integer> {
}
