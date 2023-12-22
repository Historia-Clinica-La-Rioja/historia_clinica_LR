package net.pladema.questionnaires.general.getall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.questionnaires.common.domain.QuestionnaireResponseII;

@Repository
public interface GetAllRepository extends JpaRepository<QuestionnaireResponseII, Integer> {

	@Query("SELECT qr FROM QuestionnaireResponseII qr " +
			"LEFT JOIN FETCH qr.createdByUser cu " +
			"LEFT JOIN FETCH qr.createdByHealthcareProfessional chp " +
			"WHERE qr.patientId = :patientId")
	List<QuestionnaireResponseII> findResponsesWithCreatedByDetails(@Param("patientId") Integer patientId);

}
