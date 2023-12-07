package net.pladema.questionnaires.general.getall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.questionnaires.general.getall.domain.QuestionnaireII;

@Repository
public interface QuestionnaireRepository extends JpaRepository<QuestionnaireII, Integer> {

	QuestionnaireII findByName(String name);
}
