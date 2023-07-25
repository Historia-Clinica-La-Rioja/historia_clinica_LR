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
			"JOIN Address a ON (a.id = i.addressId) " +
			"JOIN Province p ON (p.id = a.provinceId) " +
			"JOIN Department d ON (d.provinceId = p.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"WHERE d.id = :departmentId " +
			"AND di.active = TRUE AND di.clinicalSpecialtyId = :clinicalSpecialtyId AND di.deleteable.deleted = FALSE")
	List<InstitutionBasicInfoBo> getByDepartmentIdHavingActiveDiaryWithClinicalSpecialty(@Param("departmentId") Integer departmentId, @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW net.pladema.establishment.service.domain.InstitutionBasicInfoBo(i.id, i.name) " +
			"FROM Institution i " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"JOIN Province p ON (p.id = a.provinceId) " +
			"JOIN Department d ON (d.provinceId = p.id)" +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"JOIN DiaryCareLine dcl ON (dcl.pk.diaryId = di.id) " +
			"WHERE d.id = :departmentId " +
			"AND di.active = TRUE AND di.clinicalSpecialtyId = :clinicalSpecialtyId AND di.deleteable.deleted = FALSE " +
			"AND dcl.pk.careLineId = :careLineId")
	List<InstitutionBasicInfoBo> getByDepartmentIdHavingActiveDiaryWithCareLineClinicalSpecialty(@Param("departmentId") Integer departmentId, @Param("careLineId") Integer careLineId, @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

}
