package net.pladema.staff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

@Repository
public interface ClinicalSpecialtyRepository extends JpaRepository<ClinicalSpecialty, Integer>{

	@Transactional(readOnly = true)
    @Query(value = " SELECT cs "
			+ "FROM HealthcareProfessionalSpecialty hps "
            + "INNER JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
			+ "INNER JOIN ProfessionalProfessions pp ON hps.professionalProfessionId = pp.id "
            + "WHERE pp.healthcareProfessionalId = :professionalId "
			+ "AND pp.deleteable.deleted = false "
			+ "AND hps.deleteable.deleted = false")
    List<ClinicalSpecialty> getAllByProfessional(@Param("professionalId") Integer professionalId);

    @Transactional(readOnly = true)
    @Query(value = " SELECT cs FROM Diary d " +
            "JOIN ClinicalSpecialty cs "+
            "ON cs.id = d.clinicalSpecialtyId " +
            "WHERE d.id = :diaryId")
    ClinicalSpecialty getClinicalSpecialtyByDiary(@Param("diaryId") Integer diaryId);

    @Transactional(readOnly = true)
    @Query(value = " SELECT new net.pladema.staff.service.domain.ClinicalSpecialtyBo(cs.id, cs.name) " +
            "FROM ClinicalSpecialty cs " +
            "WHERE cs.id = :clinicalSpecialtyId")
    ClinicalSpecialtyBo findClinicalSpecialtyBoById(@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
	@Query(value = " SELECT cs FROM ClinicalSpecialty cs where cs.clinicalSpecialtyTypeId = 2")
	List<ClinicalSpecialty> findAllSpecialties();

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT cs " +
			"FROM ClinicalSpecialty cs " +
			"JOIN HealthcareProfessionalSpecialty hps ON (hps.clinicalSpecialtyId = cs.id) " +
			"JOIN ProfessionalProfessions pp ON (pp.id  = hps.professionalProfessionId) " +
			"JOIN HealthcareProfessional hp ON (hp.id = pp.healthcareProfessionalId) " +
			"JOIN Person p ON (p.id = hp.personId) " +
			"JOIN UserPerson up ON (p.id = up.pk.personId) " +
			"JOIN UserRole ur ON (ur.userRolePK.userId = up.pk.userId) " +
			"JOIN Diary d ON (hp.id = d.healthcareProfessionalId) " +
			"JOIN DoctorsOffice do ON (do.id = d.doctorsOfficeId) " +
			"WHERE do.institutionId = :institutionId " +
			"AND d.clinicalSpecialtyId = cs.id " +
			"AND d.active = true " +
			"AND d.endDate >= current_date() " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null) " +
			"AND hps.deleteable.deleted = false  " +
			"AND pp.deleteable.deleted = false " +
			"AND hp.deleteable.deleted = false " +
			"AND ur.deleteable.deleted = false " +
			"ORDER BY cs.name ")
	List<ClinicalSpecialty> getAllByInstitutionIdAndActiveDiaries(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT cs FROM ClinicalSpecialty as cs " +
			"JOIN CareLineInstitutionSpecialty clis ON (cs.id = clis.clinicalSpecialtyId)" +
			"JOIN CareLineInstitution cli ON (cli.id = clis.careLineInstitutionId)" +
			"JOIN DoctorsOffice do ON (do.institutionId = cli.institutionId) " +
			"JOIN Diary d ON (do.id = d.doctorsOfficeId) " +
			"JOIN DiaryCareLine dcl ON (d.id = dcl.pk.diaryId) " +
			"WHERE cli.careLineId = :careLineId " +
			"AND cli.institutionId = :destinationInstitutionId " +
			"AND d.clinicalSpecialtyId = clis.clinicalSpecialtyId " +
			"AND (d.deleteable.deleted = false) " +
			"AND (dcl.deleteable.deleted = false) " +
			"ORDER BY cs.name ")
	List<ClinicalSpecialty> getAllByCareLineIdAndDestinationInstitutionId(@Param("careLineId") Integer careLineId,
																		  @Param("destinationInstitutionId") Integer destinationInstitutionId);

}
