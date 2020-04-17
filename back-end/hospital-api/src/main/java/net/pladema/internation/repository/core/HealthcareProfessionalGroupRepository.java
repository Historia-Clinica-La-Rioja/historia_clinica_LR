package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.HealthcareProfessionalGroup;
import net.pladema.internation.repository.core.entity.HealthcareProfessionalGroupPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthcareProfessionalGroupRepository extends JpaRepository<HealthcareProfessionalGroup, HealthcareProfessionalGroupPK> {

}
