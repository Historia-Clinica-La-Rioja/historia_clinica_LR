package net.pladema.questionnaires.general.create.repository.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepositoryII extends JpaRepository<AnswerII, Integer> {
	List<AnswerII> findByQuestionnaireResponseId(Integer questionnaireResponseId);
}
