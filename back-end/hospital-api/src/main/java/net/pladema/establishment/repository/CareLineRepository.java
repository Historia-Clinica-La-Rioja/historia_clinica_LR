package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.CareLine;

import net.pladema.establishment.service.domain.CareLineBo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"JOIN CareLineInstitutionSpecialty clis ON (cli.id = clis.careLineInstitutionId)"+
			"WHERE clis.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND cli.institutionId = :institutionId " +
			"AND cli.deleted = false")
	List<CareLineBo> getCareLinesByClinicalSpecialtyAndInstitutionId(@Param("institutionId") Integer institutionId,
																	 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);
}
