package net.pladema.questionnaires.general.getall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.questionnaires.general.getall.domain.QuestionnaireResponseII;

@Repository
public interface GetAllRepository extends JpaRepository<QuestionnaireResponseII, Integer> {
	List<QuestionnaireResponseII> findByPatientId(Integer patientId);
}
