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
            "JOIN ClinicalSpecialtyCareLine cscl ON (cl.id = cscl.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
            "WHERE cscl.deleteable.deleted = false " +
			"AND cl.classified IS FALSE OR (clr.roleId IN :loggedUserRoleIds AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) " +
            "GROUP BY cl.id")
    List<CareLine> getCareLinesWhitClinicalSpecialties(@Param("loggedUserRoleIds") List<Short> loggedUserRoleIds);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"JOIN CareLineInstitutionSpecialty clis ON (cli.id = clis.careLineInstitutionId)"+
			"WHERE clis.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND cli.institutionId = :institutionId " +
			"AND cli.deleted = false " +
			"AND cl.consultation = true ")
	List<CareLineBo> getCareLinesByClinicalSpecialtyAndInstitutionId(@Param("institutionId") Integer institutionId,
																	 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"WHERE cli.deleted IS FALSE")
	List<CareLineBo> getCareLinesAttachedToInstitutions();

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine cl " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"WHERE cli.deleted IS FALSE " +
			"AND cl.classified IS FALSE OR (clr.roleId IN :loggedUserRoleIds AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE)")
	List<CareLineBo> getCareLinesAttachedToInstitutions(@Param("loggedUserRoleIds") List<Short> loggedUsedRoleIds);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine as cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"WHERE cli.institutionId = :institutionId " +
			"AND cl.deleteable.deleted IS FALSE " +
			"AND cli.deleted IS FALSE ")
	List<CareLineBo> getAllByInstitutionId(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine cl " +
			"JOIN VirtualConsultation vc ON (vc.careLineId = cl.id) " +
			"WHERE vc.institutionId = :institutionId")
	List<CareLineBo> getVirtualConsultationCareLinesByInstitutionId(@Param("institutionId") Integer institutionId);

}
