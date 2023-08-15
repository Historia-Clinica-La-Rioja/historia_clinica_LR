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

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine as cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"JOIN Institution i ON (cli.institutionId = i.id) " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"WHERE a.provinceId = :provinceId " +
			"AND cl.deleteable.deleted IS FALSE " +
			"AND cli.deleted IS FALSE ")
	List<CareLineBo> getAllByProvinceId(@Param("provinceId") Short provinceId);


	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"WHERE cli.deleted = false")
	List<CareLineBo> getCareLinesAttachedToInstitution();

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine as cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"WHERE cli.institutionId = :institutionId " +
			"AND cl.deleteable.deleted IS FALSE " +
			"AND cli.deleted IS FALSE ")
	List<CareLineBo> getAllByInstitutionId(@Param("institutionId") Integer institutionId);

}
