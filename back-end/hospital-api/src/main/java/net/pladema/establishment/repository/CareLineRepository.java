package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.CareLine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CareLineRepository extends SGXAuditableEntityJPARepository<CareLine, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT cl FROM CareLine as cl " +
            "JOIN ClinicalSpecialtyCareLine cscl " +
            "ON cl.id = cscl.careLineId " +
            "WHERE cscl.deleteable.deleted = false " +
            "GROUP BY cl.id")
    List<CareLine> getCareLinesWhitClinicalSpecialties();

}
