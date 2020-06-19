package net.pladema.clinichistory.hospitalization.repository;

import net.pladema.clinichistory.hospitalization.repository.domain.PatientDischarge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDischargeRepository extends JpaRepository<PatientDischarge, Integer> {

}
