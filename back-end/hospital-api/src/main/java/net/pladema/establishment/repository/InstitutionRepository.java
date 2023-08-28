package net.pladema.establishment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.Institution;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

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

	@Query("SELECT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) "+
			"FROM Institution i " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"WHERE c.departmentId = :departmentId " +
			"ORDER BY i.name ")
	List<InstitutionBasicInfoBo> findByDeparmentId(@Param("departmentId") Short departmentId);

	@Query("SELECT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) "+
			"FROM Institution i " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"WHERE a.provinceId = :provinceId " +
			"ORDER BY i.name ")
	List<InstitutionBasicInfoBo> findByProvinceId(@Param("provinceId") Short provinceId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) "+
			"FROM Institution i " +
			"JOIN Sector s on s.institutionId = i.id " +
			"WHERE s.sectorTypeId = '4' "+
			"AND deleted IS FALSE ")
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
			"JOIN Department d ON (a.departmentId = d.id) " +
			"JOIN Province p ON (d.provinceId = p.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"WHERE d.id = :departmentId " +
			"AND di.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.deleteable.deleted IS FALSE " +
			"AND ur.deleteable.deleted IS FALSE " +
			"AND hp.deleteable.deleted IS FALSE " +
			"AND pp.deleteable.deleted IS FALSE " +
			"AND hps.deleteable.deleted IS FALSE")
	List<InstitutionBasicInfoBo> getByDepartmentIdHavingActiveDiaryWithClinicalSpecialty(@Param("departmentId") Short departmentId,
																						 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

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
			"JOIN Department d ON (a.departmentId = d.id) " +
			"JOIN Province p ON (d.provinceId = p.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"JOIN DiaryCareLine dcl ON (dcl.pk.diaryId = di.id) " +
			"WHERE d.id = :departmentId " +
			"AND di.active = TRUE " +
			"AND di.endDate >= CURRENT_DATE " +
			"AND di.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND dcl.pk.careLineId = :careLineId " +
			"AND clis.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND cli.careLineId = :careLineId " +
			"AND cli.deleted = FALSE " +
			"AND di.deleteable.deleted IS FALSE " +
	        "AND dcl.deleteable.deleted IS FALSE " +
			"AND ur.deleteable.deleted IS FALSE " +
			"AND hp.deleteable.deleted IS FALSE " +
			"AND pp.deleteable.deleted IS FALSE " +
			"AND hps.deleteable.deleted IS FALSE")
	List<InstitutionBasicInfoBo> getByDepartmentIdHavingActiveDiaryWithCareLineClinicalSpecialty(@Param("departmentId") Short departmentId,
																								 @Param("careLineId") Integer careLineId,
																								 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

}
