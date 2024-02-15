package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.PersonalHistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalHistoryTypeRepository extends JpaRepository<PersonalHistoryType, Short> {
}
