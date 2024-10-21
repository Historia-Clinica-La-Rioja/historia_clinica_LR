package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Institution;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT i.id "+
            "FROM Institution AS i ")
    List<Integer> getAllIds();

    @Query("SELECT i " +
            "FROM Institution i " +
            "WHERE i.name LIKE :name " +
            "AND i.cuit LIKE :cuit")
    Optional<Institution> findInstitution(@Param("name") String name, @Param("cuit")  String cuit);
	@Query("SELECT i "+
			"FROM Institution AS i " +
			"WHERE i.sisaCode = :sisaCode ")
	Optional<Institution> findBySisaCode(@Param("sisaCode") String sisaCode);

	@Query("SELECT i.id "+
			"FROM Institution AS i " +
			"WHERE i.sisaCode = :sisaCode ")
	Optional<Integer> findIdBySisaCode(@Param("sisaCode") String sisaCode);

	@Query("SELECT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) "+
			"FROM Institution i " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"WHERE c.departmentId = :departmentId " +
			"ORDER BY i.name ")
	List<InstitutionBasicInfoBo> findByDepartmentId(@Param("departmentId") Short departmentId);

	@Query("SELECT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) "+
			"FROM Institution i " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"WHERE a.provinceId = :provinceId " +
			"ORDER BY i.name ")
	List<InstitutionBasicInfoBo> findByProvinceId(@Param("provinceId") Short provinceId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) "+
			"FROM Institution i " +
			"JOIN Sector s on s.institutionId = i.id " +
			"WHERE s.sectorTypeId = 4 " +
			"AND s.informer = TRUE " +
			"AND s.deleteable.deleted = FALSE ")
	List<InstitutionBasicInfoBo> getByDiagnosisImagesSectors();

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN UserRole ur ON (i.id = ur.institutionId )" +
			"JOIN UserPerson up ON (ur.userId = up.pk.userId) " +
			"JOIN HealthcareProfessional hp ON (up.pk.personId = hp.personId) " +
			"JOIN ProfessionalProfessions pp ON (hp.id = pp.healthcareProfessionalId) " +
			"JOIN HealthcareProfessionalSpecialty hps ON (pp.id = hps.professionalProfessionId) " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"WHERE c.departmentId = :departmentId " +
			"AND di.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.deleteable.deleted IS FALSE " +
			"AND ur.deleteable.deleted IS FALSE " +
			"AND hp.deleteable.deleted IS FALSE " +
			"AND pp.deleteable.deleted IS FALSE " +
			"AND hps.deleteable.deleted IS FALSE ")
	List<InstitutionBasicInfoBo> getByDepartmentIdHavingActiveDiaryWithClinicalSpecialty(@Param("departmentId") Short departmentId,
																						 @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN CareLineInstitution cli ON (i.id = cli.institutionId ) " +
			"JOIN CareLineInstitutionSpecialty clis ON (cli.id = clis.careLineInstitutionId) " +
			"JOIN UserRole ur ON (i.id = ur.institutionId )" +
			"JOIN UserPerson up ON (ur.userId = up.pk.userId) " +
			"JOIN HealthcareProfessional hp ON (up.pk.personId = hp.personId) " +
			"JOIN ProfessionalProfessions pp ON (hp.id = pp.healthcareProfessionalId) " +
			"JOIN HealthcareProfessionalSpecialty hps ON (pp.id = hps.professionalProfessionId) " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"JOIN DiaryCareLine dcl ON (dcl.pk.diaryId = di.id) " +
			"WHERE c.departmentId = :departmentId " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND dcl.pk.careLineId = :careLineId " +
			"AND clis.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND cli.careLineId = :careLineId " +
			"AND cli.deleted = FALSE " +
			"AND di.deleteable.deleted IS FALSE " +
	        "AND dcl.deleteable.deleted IS FALSE " +
			"AND ur.deleteable.deleted IS FALSE " +
			"AND hp.deleteable.deleted IS FALSE " +
			"AND pp.deleteable.deleted IS FALSE " +
			"AND hps.deleteable.deleted IS FALSE ")
	List<InstitutionBasicInfoBo> getByDepartmentIdHavingActiveDiaryWithCareLineClinicalSpecialty(@Param("departmentId") Short departmentId,
																								 @Param("careLineId") Integer careLineId,
																								 @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN VirtualConsultation vc ON (vc.institutionId = i.id) ")
	List<InstitutionBasicInfoBo> getVirtualConsultationInstitutions();

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"JOIN DiaryCareLine dcl ON (di.id = dcl.pk.diaryId) " +
			"JOIN DiaryPractice dp ON (di.id = dp.diaryId) " +
			"WHERE c.departmentId = :departmentId " +
			"AND dp.snomedId = :practiceSnomedId " +
			"AND dcl.pk.careLineId = :careLineId " +
			"AND di.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.deleteable.deleted IS FALSE " +
			"AND dcl.deleteable.deleted IS FALSE " +
			"AND dp.deleteable.deleted IS FALSE ")
	List<InstitutionBasicInfoBo> getByDepartmentAndCareLineAndPracticeAndClinicalSpecialty(@Param("departmentId") Short departmentId,
																						   @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																						   @Param("careLineId") Integer careLineId,
																						   @Param("practiceSnomedId") Integer practiceSnomedId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"JOIN DiaryCareLine dcl ON (di.id = dcl.pk.diaryId) " +
			"JOIN DiaryPractice dp ON (di.id = dp.diaryId) " +
			"WHERE c.departmentId = :departmentId " +
			"AND dp.snomedId = :practiceSnomedId " +
			"AND dcl.pk.careLineId = :careLineId " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.deleteable.deleted IS FALSE " +
			"AND dcl.deleteable.deleted IS FALSE " +
			"AND dp.deleteable.deleted IS FALSE ")
	List<InstitutionBasicInfoBo> getByDepartmentAndCareLineAndPractice(@Param("departmentId") Short departmentId,
																	   @Param("careLineId") Integer careLineId,
																	   @Param("practiceSnomedId") Integer practiceSnomedId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"JOIN DiaryPractice dp ON (di.id = dp.diaryId) " +
			"WHERE c.departmentId = :departmentId " +
			"AND dp.snomedId = :practiceSnomedId " +
			"AND di.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.deleteable.deleted IS FALSE " +
			"AND dp.deleteable.deleted IS FALSE ")
	List<InstitutionBasicInfoBo> getAllByDepartmentAndClinicalSpecialtyAndPractice(@Param("departmentId") Short departmentId,
																	  @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																	  @Param("practiceSnomedId") Integer practiceSnomedId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"JOIN DoctorsOffice do ON (i.id = do.institutionId) " +
			"JOIN Diary di ON (do.id = di.doctorsOfficeId) " +
			"JOIN DiaryPractice dp ON (di.id = dp.diaryId) " +
			"WHERE c.departmentId = :departmentId " +
			"AND dp.snomedId = :practiceSnomedId " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.deleteable.deleted IS FALSE " +
			"AND dp.deleteable.deleted IS FALSE ")
	List<InstitutionBasicInfoBo> getByDepartmentAndPractice(@Param("departmentId") Short departmentId,
															@Param("practiceSnomedId") Integer practiceSnomedId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
		  	"JOIN InstitutionalGroupInstitution igi ON (igi.institutionId = i.id) " +
		  	"JOIN InstitutionalGroup ig ON (ig.id = igi.institutionalGroupId) " +
		  	"WHERE ig.id IN :institutionalGroupsIds " +
		  	"AND ig.deleteable.deleted IS FALSE " +
		  	"AND igi.deleteable.deleted IS FALSE")
	List<InstitutionBasicInfoBo> getInstitutionsRelatedToInstitutionalGroups(@Param("institutionalGroupsIds") List<Integer> institutionalGroupsIds);

	@Transactional(readOnly = true)
	@Query("SELECT i.id "+
			"FROM Institution AS i " +
			"WHERE i.sisaCode = :sisaCode ")
	List<Integer> findIdsBySisaCode(@Param("sisaCode") String sisaCode);


	@Transactional(readOnly = true)
	@Query("SELECT i.id "+
			"FROM Institution AS i " +
			"JOIN UserRole ur ON (i.id = ur.institutionId) " +
			"WHERE ur.userId = :userId " +
			"AND ur.deleteable.deleted IS FALSE")
	List<Integer> getInstitutionsByUser(@Param("userId") Integer userId);

}
