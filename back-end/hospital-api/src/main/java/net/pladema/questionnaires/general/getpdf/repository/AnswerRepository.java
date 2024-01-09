package net.pladema.questionnaires.general.getpdf.repository;

import net.pladema.questionnaires.common.domain.Answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	List<Answer> findByQuestionnaireResponseId(Integer questionnaireResponseId);
}
