package net.pladema.medicalconsultation.diary.repository;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;


import net.pladema.medicalconsultation.diary.repository.entity.DiaryPractice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DiaryPracticeRepository extends SGXAuditableEntityJPARepository<DiaryPractice, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT dp " +
			"FROM DiaryPractice dp " +
			"WHERE dp.diaryId = :diaryId " +
			"AND dp.deleteable.deleted = false OR dp.deleteable.deleted IS NULL ")
	List<DiaryPractice> findAllByDiaryId(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo(s.id, s.sctid, s.pt) " +
			"FROM DiaryPractice dp " +
			"JOIN Snomed s ON (dp.snomedId = s.id) " +
			"WHERE dp.diaryId = :diaryId " +
			"AND dp.deleteable.deleted = false")
	List<SnomedBo> getByDiaryId(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo(s.id, s.sctid, s.pt)" +
			"FROM DiaryPractice dp " +
			"JOIN Diary d ON (dp.diaryId = d.id) " +
			"JOIN Snomed s ON (dp.snomedId = s.id) " +
			"JOIN DoctorsOffice dof ON (dof.id = d.doctorsOfficeId) " +
			"WHERE d.active = TRUE " +
			"AND dof.institutionId = :institutionId " +
			"AND d.endDate >= CURRENT_DATE " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted IS NULL)" +
			"AND (dp.deleteable.deleted = false OR dp.deleteable.deleted IS NULL) ")
	List<SnomedBo> getActiveDiariesPractices(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(" SELECT (case when count(dp.id)> 0 then true else false end)" +
			"FROM DiaryPractice dp " +
			"WHERE dp.diaryId = :diaryId " +
			"AND (dp.deleteable.deleted = false OR dp.deleteable.deleted IS NULL) ")
	Boolean hasPractices(@Param("diaryId") Integer diaryId);
}
