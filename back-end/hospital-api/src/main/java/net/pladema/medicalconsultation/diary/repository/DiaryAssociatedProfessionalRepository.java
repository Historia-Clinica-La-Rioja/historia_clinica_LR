package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.DiaryAssociatedProfessional;

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

}
