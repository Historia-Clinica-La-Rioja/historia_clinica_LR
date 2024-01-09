package net.pladema.questionnaires.familybg.get.domain.service;

import net.pladema.questionnaires.common.domain.Answer;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import org.springframework.stereotype.Service;

import net.pladema.questionnaires.familybg.create.repository.FamilyBgRepository;
import net.pladema.questionnaires.familybg.get.repository.FamilyBgSummaryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GetFamilyBgServiceImpl implements GetFamilyBgService {
	private final FamilyBgRepository familyBgRepository;

	private final FamilyBgSummaryRepository questionnaireSummaryRepository;

	public GetFamilyBgServiceImpl(FamilyBgRepository familyBgRepository, FamilyBgSummaryRepository questionnaireSummaryRepository) {
		this.familyBgRepository =  familyBgRepository;
		this.questionnaireSummaryRepository = questionnaireSummaryRepository;
	}

	public List<Answer> findPatientQuestionnaire(Integer patientId) {
		return this.familyBgRepository.findPatientFamilyBgTest(patientId);
	}

	public Optional<QuestionnaireSummary> findQuestionnaireSummary(Integer questionnaireId) {
		return this.questionnaireSummaryRepository.getQuestionnaireSummaryReport(questionnaireId);
	}
}
