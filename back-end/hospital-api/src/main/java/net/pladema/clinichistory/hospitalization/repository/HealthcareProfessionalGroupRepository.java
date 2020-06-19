package net.pladema.clinichistory.hospitalization.repository;

import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroupPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthcareProfessionalGroupRepository extends JpaRepository<HealthcareProfessionalGroup, HealthcareProfessionalGroupPK> {

}
