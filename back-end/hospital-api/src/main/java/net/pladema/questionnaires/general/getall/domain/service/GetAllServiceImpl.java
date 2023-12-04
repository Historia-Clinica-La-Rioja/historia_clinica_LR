package net.pladema.questionnaires.general.getall.domain.service;

import net.pladema.questionnaires.common.dto.QuestionnaireInfo;
import net.pladema.questionnaires.general.getall.repository.GetAllRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllServiceImpl implements GetAllService {
	private final GetAllRepository getAllRepository;
	
	public GetAllServiceImpl(GetAllRepository getAllRepository) {
		this.getAllRepository = getAllRepository;
	}
	
	public List<QuestionnaireInfo> findQuestionnairesInfo(Integer patientId) {
		return getAllRepository.getQuestionnairesInfo(patientId);
	}

}
