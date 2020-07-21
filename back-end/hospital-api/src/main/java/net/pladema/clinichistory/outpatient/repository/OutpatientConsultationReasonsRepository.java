package net.pladema.clinichistory.outpatient.repository;

import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultationReasons;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultationReasonsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutpatientConsultationReasonsRepository extends JpaRepository<OutpatientConsultationReasons, OutpatientConsultationReasonsPK> {

}
