package net.pladema.medicalconsultation.diary.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.repository.domain.ActiveDiaryClinicalSpecialtyVo;
import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;

import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

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
    @Query("SELECT d " +
            "FROM Diary AS d " +
			"JOIN DoctorsOffice dof ON (d.doctorsOfficeId = dof.id) " +
			"WHERE (d.healthcareProfessionalId = :hpId OR d.doctorsOfficeId = :doId) " +
            "AND d.startDate <= :endDate " +
            "AND d.endDate >= :startDate " +
			"AND dof.institutionId = :institutionId " +
            "AND d.deleteable.deleted = false " +
			"AND NOT EXISTS (SELECT dap.id FROM DiaryAssociatedProfessional dap WHERE dap.diaryId = d.id)")
	List<Diary> findAllOverlappingDiary(@Param("hpId") Integer healthcareProfessionalId,
										@Param("doId") Integer doctorsOfficeId,
										@Param("institutionId") Integer institutionId,
										@Param("startDate") LocalDate newDiaryStart,
                                        @Param("endDate") LocalDate newDiaryEnd);

	@Transactional(readOnly = true)
	@Query("SELECT d " +
			"FROM Diary AS d " +
			"JOIN DoctorsOffice dof ON (d.doctorsOfficeId = dof.id) " +
			"WHERE (d.healthcareProfessionalId = :hpId OR d.doctorsOfficeId = :doId) " +
			"AND d.startDate <= :endDate " +
			"AND d.endDate >= :startDate " +
			"AND d.id <> :excludeDiaryId " +
			"AND dof.institutionId = :institutionId " +
			"AND d.deleteable.deleted = false " +
			"AND d.endDate > CURRENT_DATE " +
			"AND NOT EXISTS (SELECT dap.id FROM DiaryAssociatedProfessional dap WHERE dap.diaryId = d.id)")
	List<Diary> findAllOverlappingDiaryExcludingDiary(@Param("hpId") Integer healthcareProfessionalId,
													  @Param("doId") Integer doctorsOfficeId,
													  @Param("institutionId") Integer institutionId,
													  @Param("startDate") LocalDate newDiaryStart,
													  @Param("endDate") LocalDate newDiaryEnd,
													  @Param("excludeDiaryId") Integer excludeDiaryId);
    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
            "d, do.description, cs.name) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
			"LEFT JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
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
			"LEFT JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
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
            "d, do.description, s.id, s.description, d.healthcareProfessionalId, cs.name, hu.alias) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice do ON do.id = d.doctorsOfficeId " +
			"JOIN Sector s ON s.id = do.sectorId " +
			"LEFT JOIN ClinicalSpecialty cs ON cs.id = d.clinicalSpecialtyId " +
			"LEFT JOIN HierarchicalUnit hu ON (d.hierarchicalUnitId = hu.id) " +
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
	@Query(" SELECT DISTINCT NEW net.pladema.medicalconsultation.diary.repository.domain.ActiveDiaryClinicalSpecialtyVo(" +
	 		" cs.id, cs.name" +
	 		") " +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"WHERE d.active = TRUE " +
			"AND dof.institutionId = :institutionId " +
			"AND d.endDate >= CURRENT_DATE")
	List<ActiveDiaryClinicalSpecialtyVo> getActiveDiariesClinicalSpecialties(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName, p.middleNames,p.otherLastNames,pe.nameSelfDetermination)" +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"INNER JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND (d.alias = :aliasOrClinicalSpecialtyName " +
			"OR cs.name = :aliasOrClinicalSpecialtyName ) " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND d.deleteable.deleted IS NOT TRUE " +
			"AND NOT EXISTS (SELECT 1 FROM DiaryPractice dp WHERE dp.diaryId = d.id)")
	List<CompleteDiaryListVo> getActiveDiariesByAliasOrClinicalSpecialtyName(@Param("institutionId") Integer institutionId,
																			 @Param("aliasOrClinicalSpecialtyName") String aliasOrClinicalSpecialtyName);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
			"d, dof.description, cs.name)" +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"WHERE d.active = TRUE " +
			"AND d.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND dof.institutionId = :institutionId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND (NOT EXISTS(SELECT 1 FROM DiaryPractice dp WHERE (d.id = dp.diaryId)))"
	)
	List<DiaryListVo> getActiveDiariesWithoutPracticesByInstitutionAndSpecialty(@Param("institutionId") Integer institutionId,
																				@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
			"d, dof.description, cs.name)" +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"WHERE d.active = TRUE " +
			"AND d.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND dof.institutionId = :institutionId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND (EXISTS(SELECT 1 FROM DiaryPractice dp WHERE (d.id = dp.diaryId)))"
	)
	List<DiaryListVo> getActiveDiariesWithPracticesByInstitutionAndSpecialty(@Param("institutionId") Integer institutionId,
																@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);
	@Transactional(readOnly = true)
	@Query(" SELECT d " +
			"FROM Diary d " +
			"WHERE doctorsOfficeId = :doctorsOfficeId " +
			"AND deleted = :isDeleted ")
	List<Diary> findByDoctorsOfficeId(@Param("doctorsOfficeId") Integer doctorsOfficeId, @Param("isDeleted") Boolean isDeleted);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, do.description, s.id, s.description, d.healthcareProfessionalId, cs.name) " +
			"FROM Diary d " +
			"JOIN DoctorsOffice do ON do.id = d.doctorsOfficeId " +
			"JOIN Sector s ON s.id = do.sectorId " +
			"JOIN ClinicalSpecialty cs ON cs.id = d.clinicalSpecialtyId " +
			"JOIN AppointmentAssn aa ON aa.pk.diaryId = d.id " +
			"WHERE aa.pk.appointmentId = :appointmentId ")
	Optional<CompleteDiaryListVo> getCompleteDiaryByAppointment(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName, p.middleNames,p.otherLastNames,pe.nameSelfDetermination)" +
			"FROM Diary d " +
			"LEFT JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"LEFT JOIN DiaryPractice dp ON (d.id = dp.diaryId) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"INNER JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND dp.snomedId = :practiceId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted IS NULL) " +
			"AND (dp.deleteable.deleted = false OR dp.deleteable.deleted IS NULL)")
	List<CompleteDiaryListVo> getActiveDiariesByPracticeId(
			@Param("institutionId") Integer institutionId,
			@Param("practiceId") Integer practiceId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName, p.middleNames,p.otherLastNames,pe.nameSelfDetermination)" +
			"FROM Diary d " +
			"LEFT JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"LEFT JOIN DiaryPractice dp ON (d.id = dp.diaryId) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"INNER JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND dp.snomedId = :practiceId " +
			"AND (d.alias = :aliasOrClinicalSpecialtyName " +
			"OR cs.name = :aliasOrClinicalSpecialtyName ) " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted IS NULL) " +
			"AND (dp.deleteable.deleted = false OR dp.deleteable.deleted IS NULL)")
	List<CompleteDiaryListVo> getActiveDiariesByAliasOrClinicalSpecialtyNameAndPracticeId(
			@Param("institutionId") Integer institutionId,
			@Param("aliasOrClinicalSpecialtyName") String aliasOrClinicalSpecialtyName,
			@Param("practiceId") Integer practiceId);

	@Transactional(readOnly = true)
	@Query(" SELECT d.appointmentDuration " +
			"FROM Diary d " +
			"WHERE d.id = :diaryId")
	Short getDiaryAppointmentDuration(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.service.domain.DiaryBo(d.endDate, d.appointmentDuration) " +
			"FROM Diary d " +
			"WHERE d.id = :diaryId")
	DiaryBo getDiaryEndDateAndAppointmentDuration(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.service.domain.DiaryBo(d.startDate, d.endDate) " +
			"FROM Diary d " +
			"WHERE d.id = :diaryId")
	DiaryBo getDiaryStartAndEndDate(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName, p.middleNames,p.otherLastNames,pe.nameSelfDetermination)" +
			"FROM Diary d " +
			"LEFT JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"LEFT JOIN DiaryPractice dp ON (d.id = dp.diaryId) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"INNER JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND dp.snomedId = :practiceId " +
			"AND d.id = :diaryId " +
			"AND cs.id = :clinicalSpecialtyId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted IS NULL) " +
			"AND (dp.deleteable.deleted = false OR dp.deleteable.deleted IS NULL)")
	List<CompleteDiaryListVo> getActiveDiariesByClinicalSpecialtyIdAndDiaryIdAndPracticeId(
		@Param("institutionId") Integer institutionId,
		@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
		@Param("diaryId") Integer diaryId,
		@Param("practiceId") Integer practiceId
	);

	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName, p.middleNames,p.otherLastNames,pe.nameSelfDetermination)" +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"INNER JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND d.id = :diaryId " +
			"AND cs.id = :clinicalSpecialtyId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND d.deleteable.deleted IS NOT TRUE " +
			"AND NOT EXISTS (SELECT 1 FROM DiaryPractice dp WHERE dp.diaryId = d.id)")
	List<CompleteDiaryListVo> getActiveDiariesByClinicalSpecialtyIdAndDiaryId(
		@Param("institutionId") Integer institutionId,
		@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
		@Param("diaryId") Integer diaryId
	);

	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName, p.middleNames,p.otherLastNames,pe.nameSelfDetermination)" +
			"FROM Diary d " +
			"INNER JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"INNER JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND cs.id = :clinicalSpecialtyId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND d.deleteable.deleted IS NOT TRUE " +
			"AND NOT EXISTS (SELECT 1 FROM DiaryPractice dp WHERE dp.diaryId = d.id)")
	List<CompleteDiaryListVo> getActiveDiariesByClinicalSpecialtyId(
		@Param("institutionId") Integer institutionId,
		@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId
	);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
			"d, dof.description, dof.sectorId, d.healthcareProfessionalId, cs.name, p.firstName, p.lastName, p.middleNames, p.otherLastNames, pe.nameSelfDetermination)" +
			"FROM Diary d " +
			"LEFT JOIN ClinicalSpecialty cs ON (d.clinicalSpecialtyId = cs.id) " +
			"LEFT JOIN DiaryPractice dp ON (d.id = dp.diaryId) " +
			"INNER JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"INNER JOIN HealthcareProfessional hp ON (hp.id = d.healthcareProfessionalId) " +
			"INNER JOIN Person p ON (p.id = hp.personId) " +
			"INNER JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE d.active = true " +
			"AND dof.institutionId = :institutionId " +
			"AND dp.snomedId = :practiceId " +
			"AND cs.id = :clinicalSpecialtyId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted IS NULL) " +
			"AND (dp.deleteable.deleted = false OR dp.deleteable.deleted IS NULL)")
	List<CompleteDiaryListVo> getActiveDiariesByClinicalSpecialtyIdAndPracticeId(
		@Param("institutionId") Integer institutionId,
		@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
		@Param("practiceId") Integer practiceId
	);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo(a.id, a.dateTypeId, a.hour, " +
			"a.appointmentStateId, doh.medicalAttentionTypeId, a.isOverturn, doh.pk.openingHoursId, a.patientId, doh.pk.openingHoursId, op.dayWeekId) " +
			"FROM Diary d " +
			"JOIN AppointmentAssn aa ON (aa.pk.diaryId = d.id) " +
			"JOIN Appointment a ON (a.id = aa.pk.appointmentId) " +
			"JOIN DiaryOpeningHours doh ON (doh.pk.diaryId = d.id AND doh.pk.openingHoursId = aa.pk.openingHoursId) " +
			"JOIN OpeningHours op ON op.id = doh.pk.openingHoursId " +
			"WHERE d.id = :diaryId " +
			"AND a.appointmentStateId != :appointmentStateId " +
			"AND a.deleteable.deleted = FALSE " +
			"AND d.deleteable.deleted = FALSE")
	List<UpdateDiaryAppointmentBo> fetchUpdateDiaryAppointments(@Param("diaryId") Integer diaryId,
																@Param("appointmentStateId") Short appointmentStateId);

	@Transactional(readOnly = true)
	@Query("SELECT d.doctorsOfficeId " +
			"FROM Diary AS d " +
			"WHERE d.deleteable.deleted = FALSE " +
			"AND d.id = :diaryId")
	Optional<Integer> findDoctorsOfficeByDiaryId(@Param("diaryId") Integer diaryId);

}
