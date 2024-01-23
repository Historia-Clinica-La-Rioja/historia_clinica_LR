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

}
