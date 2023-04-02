package net.pladema.edMonton.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.edMonton.repository.domain.Answer;
import net.pladema.edMonton.repository.domain.QuestionnaireResponse;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface EdMontonRepository extends SGXAuditableEntityJPARepository<QuestionnaireResponse, Integer> {

	@Query(value = "SELECT new net.pladema.edMonton.repository.domain.Answer(la.id, la.itemId, la.questionnaireResponseId, la.answerId) " +
			"FROM Answer la " +
			"INNER JOIN QuestionnaireResponse qr ON  qr.id = la.questionnaireResponseId " +
			"WHERE qr.patientId = :patientId ")
	public List<Answer> findPatientEdMontonTest(@Param("patientId") Integer patientId);
}