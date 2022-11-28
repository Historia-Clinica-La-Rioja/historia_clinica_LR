package net.pladema.medicalconsultation.diary.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends SGXAuditableEntityJPARepository<Diary, Integer> {


    @Transactional(readOnly = true)
    @Query("SELECT id " +
            "FROM Diary " +
            "WHERE healthcareProfessionalId = :hcpId " +
            "AND doctorsOfficeId = :doId " +
            "AND startDate <= :endDate " +
            "AND endDate >= :startDate " +
	    	"AND appointmentDuration = :appointmentDuration " +
            "AND deleteable.deleted = false")
    List<Integer> findAllOverlappingDiaryByProfessional(@Param("hcpId") Integer healthcareProfessionalId,
                                                        @Param("doId") Integer doctorsOfficeId,
                                                        @Param("startDate") LocalDate newDiaryStart,
                                                        @Param("endDate") LocalDate newDiaryEnd,
														@Param("appointmentDuration") Short appointmentDuration);
    
    @Transactional(readOnly = true)
    @Query("SELECT id " +
            "FROM Diary " +
            "WHERE healthcareProfessionalId = :hcpId " +
            "AND doctorsOfficeId = :doId " +
            "AND startDate <= :endDate " +
            "AND endDate >= :startDate " +
	    	"AND appointmentDuration = :appointmentDuration " +
            "AND id <> :excludeDiaryId " +
            "AND deleteable.deleted = false")
    List<Integer> findAllOverlappingDiaryByProfessionalExcludingDiary(@Param("hcpId") Integer healthcareProfessionalId,
                                                                      @Param("doId") Integer doctorsOfficeId,
                                                                      @Param("startDate") LocalDate newDiaryStart,
                                                                      @Param("endDate") LocalDate newDiaryEnd,
																	  @Param("appointmentDuration") Short appointmentDuration,
                                                                      @Param("excludeDiaryId") Integer excludeDiaryId);


    @Transactional(readOnly = true)
    @Query("SELECT d " +
            "FROM Diary AS d " +
            "WHERE d.doctorsOfficeId = :doId " +
            "AND d.startDate <= :endDate " +
            "AND d.endDate >= :startDate " +
            "AND d.deleteable.deleted = false")
    List<Diary> findAllOverlappingDiary(@Param("doId") Integer doctorsOfficeId,
                                          @Param("startDate") LocalDate newDiaryStart,
                                          @Param("endDate") LocalDate newDiaryEnd);

    @Transactional(readOnly = true)
    @Query("SELECT d " +
            "FROM Diary AS d " +
            "WHERE d.doctorsOfficeId = :doId " +
            "AND d.startDate <= :endDate " +
            "AND d.endDate >= :startDate " +
            "AND d.id <> :excludeDiaryId " +
            "AND d.deleteable.deleted = false")
    List<Diary> findAllOverlappingDiaryExcludingDiary(@Param("doId") Integer doctorsOfficeId,
                                                        @Param("startDate") LocalDate newDiaryStart,
                                                        @Param("endDate") LocalDate newDiaryEnd,
                                                        @Param("excludeDiaryId") Integer excludeDiaryId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
            "d, do.description, cs.name) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
			"JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
            "WHERE d.healthcareProfessionalId = :hcpId " +
            "AND do.institutionId = :instId " +
            "AND d.active = true "+
            "AND d.deleteable.deleted = false")
    List<DiaryListVo> getActiveDiariesFromProfessional(@Param("hcpId") Integer healthcareProfessionalId, @Param("instId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
			"d, do.description, cs.name) " +
			"FROM Diary d " +
			"JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
			"JOIN DiaryAssociatedProfessional AS dap ON (dap.diaryId = d.id)  " +
			"JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
			"WHERE d.healthcareProfessionalId = :healthcareProfessionalId " +
			"AND dap.healthcareProfessionalId = :associatedHealthcareProfessionalId " +
			"AND do.institutionId = :institutionId " +
			"AND d.active = true "+
			"AND d.deleteable.deleted = false")
	List<DiaryListVo> getActiveAssociatedDiariesFromProfessional(@Param("associatedHealthcareProfessionalId") Integer associatedHealthcareProfessionalId,
																 @Param("healthcareProfessionalId") Integer healthcareProfessionalId,
																 @Param("institutionId") Integer institutionId);


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
            "d, do.description, cs.name) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
			"JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
            "WHERE d.healthcareProfessionalId = :hcpId " +
            "AND d.clinicalSpecialtyId = :specialtyId " +
            "AND do.institutionId = :instId " +
            "AND d.active = true "+
            "AND d.deleteable.deleted = false")
    List<DiaryListVo> getActiveDiariesFromProfessionalAndSpecialty(
            @Param("hcpId") Integer healthcareProfessionalId,
            @Param("specialtyId") Integer specialtyId,
            @Param("instId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
			"d, do.description, cs.name) " +
			"FROM Diary d " +
			"JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
			"JOIN DiaryAssociatedProfessional AS dap ON (dap.diaryId = d.id) " +
			"JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
			"WHERE d.healthcareProfessionalId = :healthcareProfessionalId " +
			"AND dap.healthcareProfessionalId = :associatedHealthcareProfessionalId " +
			"AND d.clinicalSpecialtyId = :specialtyId " +
			"AND do.institutionId = :institutionId " +
			"AND d.active = true "+
			"AND d.deleteable.deleted = false")
	List<DiaryListVo> getActiveAssociatedDiariesFromProfessionalAndSpecialty(@Param("associatedHealthcareProfessionalId") Integer associatedHealthcareProfessionalId,
																			 @Param("healthcareProfessionalId") Integer healthcareProfessionalId,
																			 @Param("specialtyId") Integer specialtyId,
																			 @Param("institutionId") Integer institutionId);
    
    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
            "d, do.description, s.id, s.description, d.healthcareProfessionalId, cs.name) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice do ON do.id = d.doctorsOfficeId " +
			"JOIN Sector s ON s.id = do.sectorId " +
			"JOIN ClinicalSpecialty cs ON cs.id = d.clinicalSpecialtyId " +
            "WHERE d.id = :diaryId ")
    Optional<CompleteDiaryListVo> getDiary(@Param("diaryId") Integer diaryId);
    
    @Transactional(readOnly = true)
    @Query("SELECT d " +
            "FROM Diary d " +
            "JOIN AppointmentAssn aa ON aa.pk.diaryId = d.id " +
            "WHERE aa.pk.appointmentId = :appointmentId ")
    Optional<Diary> getDiaryByAppointment(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query("SELECT (case when count(d.id)> 0 then true else false end) " +
			"FROM Diary d " +
			"LEFT JOIN DiaryAssociatedProfessional dap ON (d.id = dap.diaryId)" +
			"JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
			"WHERE (d.healthcareProfessionalId = :healthcareProfessionalId " +
			"OR dap.healthcareProfessionalId = :healthcareProfessionalId) " +
			"AND do.institutionId = :institutionId " +
			"AND d.active = true " +
			"AND d.endDate >= current_date() " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)")
	boolean hasActiveDiariesInInstitution(@Param("healthcareProfessionalId")Integer healthcareProfessionalId, @Param("institutionId") Integer institutionId);
	
        @Transactional(readOnly = true)
        @Query("SELECT s.institutionId " +
			"FROM Diary d " +
			"JOIN DoctorsOffice do ON d.doctorsOfficeId = do.id " +
			"JOIN Sector s ON do.sectorId = s.id " +
			"WHERE d.id = :diaryId ")
	Optional<Integer> getInstitutionIdByDiary(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT (CASE WHEN d.alias IS NULL THEN cs.name ELSE d.alias END) AS alias " +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"WHERE d.active = TRUE " +
			"AND dof.institutionId = :institutionId " +
			"AND d.endDate >= CURRENT_DATE")
	List<String> getActiveDiariesAliases(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName)" +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND (d.alias = :aliasOrClinicalSpecialtyName " +
			"OR cs.name = :aliasOrClinicalSpecialtyName) " +
			"AND d.endDate >= CURRENT_DATE ")
	List<CompleteDiaryListVo> getActiveDiariesByAliasOrClinicalSpecialtyName(
			@Param("institutionId") Integer institutionId,
			@Param("aliasOrClinicalSpecialtyName") String aliasOrClinicalSpecialtyName
	);
}
