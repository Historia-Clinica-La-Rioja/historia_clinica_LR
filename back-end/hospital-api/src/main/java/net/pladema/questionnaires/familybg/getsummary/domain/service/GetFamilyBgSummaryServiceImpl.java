package net.pladema.questionnaires.familybg.getsummary.domain.service;

import net.pladema.questionnaires.common.domain.Answer;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import org.springframework.stereotype.Service;

import net.pladema.questionnaires.familybg.create.repository.FamilyBgRepository;
import net.pladema.questionnaires.familybg.get.domain.service.GetFamilyBgService;
import net.pladema.questionnaires.familybg.getsummary.repository.FamilyBgSummaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GetFamilyBgSummaryServiceImpl implements GetFamilyBgService {
	private final FamilyBgRepository familyBgRepository;

	private final FamilyBgSummaryRepository questionnaireSummaryRepository;

	public GetFamilyBgSummaryServiceImpl(FamilyBgRepository familyBgRepository, FamilyBgSummaryRepository questionnaireSummaryRepository) {
		this.familyBgRepository =  familyBgRepository;
		this.questionnaireSummaryRepository = questionnaireSummaryRepository;
	}

	public List<Answer> findPatientQuestionnaire(Integer patientId) {
		return this.familyBgRepository.findPatientFamilyBgTest(patientId);
	}

	public Optional<QuestionnaireSummary> findQuestionnaireSummary(Integer questionnaireId) {
		return this.questionnaireSummaryRepository.getQuestionnaireSumaryReport(questionnaireId);
	}
}
