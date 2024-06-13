package net.pladema.questionnaires.common.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;

@Repository
public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Integer> {

	@Query("SELECT qr FROM QuestionnaireResponse qr " +
			"LEFT JOIN FETCH qr.createdByHealthcareProfessional chp " +
			"WHERE qr.patientId = :patientId")
	List<QuestionnaireResponse> findResponsesWithCreatedByDetails(@Param("patientId") Integer patientId, Sort sort);

	@Query(value = "SELECT op.value " +
			"FROM minsal_lr_questionnaire_response qr " +
			"INNER JOIN minsal_lr_questionnaire q on qr.questionnaire_id=q.id " +
			"INNER JOIN minsal_lr_answer a on a.questionnaire_response_id=qr.id " +
			"INNER JOIN minsal_lr_item i on a.item_id=i.id " +
			"LEFT JOIN minsal_lr_option op on a.option_id=op.id " +
			"WHERE qr.id = :questionnaireResponseId " +
			"ORDER BY i.id DESC " +
			"LIMIT 1",
	nativeQuery = true)
	String findLastOptionValueByQuestionnaireResponseId(@Param("questionnaireResponseId") Integer questionnaireResponseId);

}
