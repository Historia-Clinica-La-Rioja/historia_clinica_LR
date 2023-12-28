package net.pladema.questionnaires.familybg.create.repository;

import java.util.List;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.questionnaires.common.domain.QuestionnaireResponse;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.questionnaires.common.domain.Answer;

public interface FamilyBgRepository extends SGXAuditableEntityJPARepository<QuestionnaireResponse, Integer> {

	@Query(value = "SELECT new net.pladema.questionnaires.common.domain.Answer(qr.id, la.itemId, la.questionnaireResponseId, la.value, la.answerId) " +
			"FROM Answer la " +
			"INNER JOIN QuestionnaireResponse qr ON  qr.id = la.questionnaireResponseId " +
			"WHERE qr.patientId = :patientId " +
			"AND qr.statusId = 2 " +
			"ORDER BY qr.id, la.itemId, la.questionnaireResponseId, la.answerId, la.value" )
	List<Answer> findPatientFamilyBgTest(@Param("patientId") Integer patientId);

	@Modifying
	@Transactional
	@Query("UPDATE QuestionnaireResponse e SET e.statusId = :newStatus WHERE e.id = :questionnaireResponseId")
	void updateStatusById(@Param("questionnaireResponseId") Integer questionnaireResponseId, @Param("newStatus") Integer newStatus);
}
