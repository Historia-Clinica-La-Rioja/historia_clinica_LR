package net.pladema.questionnaires.frail.create.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.domain.QuestionnaireResponse;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FrailRepository extends SGXAuditableEntityJPARepository<QuestionnaireResponse, Integer> {

	@Query(value = "SELECT new net.pladema.questionnaires.common.domain.Answer(qr.id, la.itemId, la.questionnaireResponseId, la.answerId) " +
			"FROM Answer la " +
			"INNER JOIN QuestionnaireResponse qr ON  qr.id = la.questionnaireResponseId " +
			"WHERE qr.patientId = :patientId " +
			"AND qr.statusId = 2" +
			"ORDER BY qr.id, la.itemId, la.questionnaireResponseId, la.answerId" )
	List<Answer> findPatientFrailTest(@Param("patientId") Integer patientId);

	@Modifying
	@Transactional
	@Query("UPDATE QuestionnaireResponse e SET e.statusId = :newStatus WHERE e.id = :questionnaireResponseId")
	void updateStatusById(@Param("questionnaireResponseId") Integer questionnaireResponseId, @Param("newStatus") Integer newStatus);
}
