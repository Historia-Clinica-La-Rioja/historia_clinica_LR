package net.pladema.clinichistory.outpatient.repository;

import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutpatientConsultationRepository extends JpaRepository<OutpatientConsultation, Integer> {

}