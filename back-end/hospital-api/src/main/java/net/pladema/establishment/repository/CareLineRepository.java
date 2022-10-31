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
			"JOIN CareLineProblem clp ON (cl.id = clp.careLineId) " +
			"JOIN Snomed s ON (clp.snomedId = s.id) " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"JOIN DoctorsOffice do ON (do.institutionId = cli.institutionId) " +
			"JOIN Diary d ON (do.id = d.doctorsOfficeId) " +
			"JOIN DiaryCareLine dcl ON (d.id = dcl.pk.diaryId) " +
			"WHERE s.sctid IN :problemSnomedIds " +
			"AND cli.institutionId = :destinationInstitutionId " +
			"AND dcl.pk.careLineId = cl.id " +
			"AND cli.deleted = false " +
			"AND d.active = true " +
			"AND d.endDate >= current_date() " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null) " +
			"ORDER BY cl.description" )
	List<CareLineBo> getCareLinesByProblemsSctidsAndDestinationInstitutionIdWithActiveDiaries(@Param("problemSnomedIds") List<String> problemSnomedIds,
																							  @Param("destinationInstitutionId") Integer destinationInstitutionId);


	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM CareLine cl " +
			"JOIN CareLineInstitution cli ON (cl.id = cli.careLineId) " +
			"WHERE cli.deleted = false")
	List<CareLineBo> getCareLinesAttachedToInstitution();

}
