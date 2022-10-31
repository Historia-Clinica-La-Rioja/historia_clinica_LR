package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.DiaryAssociatedProfessional;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DiaryAssociatedProfessionalRepository extends JpaRepository<DiaryAssociatedProfessional, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT dap " +
			"FROM DiaryAssociatedProfessional as dap " +
			"WHERE dap.diaryId = :diaryId")
	List<DiaryAssociatedProfessional> getDiaryAssociatedProfessionalsByDiary(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d.healthcareProfessionalId " +
			"FROM Diary d " +
			"JOIN DoctorsOffice do ON (d.doctorsOfficeId = do.id) " +
			"WHERE d.id IN (SELECT DISTINCT dap.diaryId " +
			"FROM DiaryAssociatedProfessional dap  " +
			"WHERE dap.healthcareProfessionalId = :healthcareProfessionalId) " +
			"AND d.deleteable.deleted = false " +
			"AND do.institutionId = :institutionId ")
	List<Integer> getAllAssociatedWithHealthcareProfessionalsIdByHealthcareProfessional(@Param("institutionId") Integer institutionId,
																						@Param("healthcareProfessionalId") Integer healthcareProfessionalId);


	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo( hp.id, " +
			"p.firstName, p.lastName, pe.nameSelfDetermination) " +
			"FROM DiaryAssociatedProfessional as dap " +
			"JOIN HealthcareProfessional as hp ON (dap.healthcareProfessionalId = hp.id ) " +
			"JOIN Person p ON (hp.personId = p.id) " +
			"JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE dap.diaryId = :diaryId")
	List<ProfessionalPersonBo> getDiaryAssociatedProfessionalsInfo(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d.healthcareProfessionalId " +
			"FROM Diary d " +
			"JOIN DoctorsOffice do ON (d.doctorsOfficeId = do.id) " +
			"WHERE d.id IN (SELECT DISTINCT dap.diaryId " +
			"FROM DiaryAssociatedProfessional dap  " +
			"WHERE dap.healthcareProfessionalId = :healthcareProfessionalId) " +
			"AND d.deleteable.deleted = false " +
			"AND do.institutionId = :institutionId " +
			"AND d.active = true " +
			"AND d.endDate >= current_date() ")
	List<Integer> getAllAssociatedWithHealthcareProfessionalsIdAndActiveDiariesByHealthcareProfessional(@Param("institutionId") Integer institutionId,
																										@Param("healthcareProfessionalId") Integer healthcareProfessionalId);

}
