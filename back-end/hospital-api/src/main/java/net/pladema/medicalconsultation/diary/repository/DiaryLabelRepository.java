package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.DiaryLabel;

import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DiaryLabelRepository extends JpaRepository<DiaryLabel, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo(dl.id, dl.diaryId, dl.colorId, dl.description) " +
			"FROM DiaryLabel dl " +
			"WHERE dl.diaryId = :diaryId")
	List<DiaryLabelBo> getDiaryLabels(@Param("diaryId") Integer diaryId);

	@Transactional
	@Modifying
	@Query("DELETE " +
			"FROM DiaryLabel dl " +
			"WHERE dl.diaryId = :diaryId " +
			"AND dl.id NOT IN :ids")
	void deleteDiaryLabels(@Param("diaryId") Integer diaryId, @Param("ids") List<Integer> ids);
}
