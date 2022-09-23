package net.pladema.medicalconsultation.diary.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.service.domain.CareLineBo;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryCareLine;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryCareLinePk;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiaryCareLineRepository extends SGXAuditableEntityJPARepository<DiaryCareLine, DiaryCareLinePk> {

	@Transactional(readOnly = true)
	@Query("SELECT dcl " +
			"FROM DiaryCareLine dcl " +
			"WHERE dcl.pk.diaryId = :diaryId ")
	List<DiaryCareLine> findAllByDiaryId(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.service.domain.CareLineBo(cl.id, cl.description) " +
			"FROM DiaryCareLine dcl " +
			"JOIN CareLine cl ON (dcl.pk.careLineId = cl.id) " +
			"WHERE dcl.pk.diaryId = :diaryId " +
			"AND dcl.deleteable.deleted = false")
	List<CareLineBo> getCareLinesByDiaryId(@Param("diaryId") Integer diaryId);

	@Query("SELECT dcl.pk.careLineId " +
			"FROM DiaryCareLine dcl " +
			"WHERE dcl.pk.diaryId = :diaryId " +
			"AND dcl.deleteable.deleted = false")
	List<Integer> getCareLineIdsByDiary(@Param("diaryId") Integer diaryId);
}
