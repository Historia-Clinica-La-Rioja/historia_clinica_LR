package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceTypeRepository extends JpaRepository<SourceType, Short> {

}
