package net.pladema.questionnaires.common.repository;

import java.util.List;

import net.pladema.questionnaires.common.repository.entity.Answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	List<Answer> findByQuestionnaireResponseId(Integer questionnaireResponseId);
}
