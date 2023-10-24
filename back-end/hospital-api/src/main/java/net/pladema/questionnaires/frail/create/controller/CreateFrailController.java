package net.pladema.questionnaires.frail.create.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.questionnaires.common.domain.model.QuestionnaireAnswerBO;
import net.pladema.questionnaires.common.domain.model.QuestionnaireBO;
import net.pladema.questionnaires.common.dto.CreateQuestionnaireDTO;
import net.pladema.questionnaires.common.dto.QuestionnaireAnswerDTO;
import net.pladema.questionnaires.frail.create.domain.EFrailTestAnswer;
import net.pladema.questionnaires.frail.create.domain.service.CreateFrailService;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class CreateFrailController implements CreateFrailAPI {

	private static final Logger logger = LoggerFactory.getLogger(CreateFrailController.class);

	private final CreateFrailService createFrailService;

	public CreateFrailController(CreateFrailService createFrailService) {
		this.createFrailService = createFrailService;
	}

	@Override
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> createPatientFrail(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "patientId") Integer patientId, CreateQuestionnaireDTO createFrailDTO) {
		QuestionnaireBO frailBO = createFrailDTO(patientId, createFrailDTO);

		createFrailService.execute(frailBO);

		logger.debug("Frail scale test created successfully.");

		return ResponseEntity.ok().body(true);
	}

	private QuestionnaireBO createFrailDTO(Integer patientId, CreateQuestionnaireDTO createFrailDTO) {
		QuestionnaireBO reg = new QuestionnaireBO();
		QuestionnaireAnswerBO lstReg;
		reg.setPatientId(patientId);
		if (createFrailDTO.getQuestionnaire() != null && !createFrailDTO.getQuestionnaire().isEmpty()) {
			reg.setAnswers(new ArrayList<>());
			for (QuestionnaireAnswerDTO dto : createFrailDTO.getQuestionnaire()) {
				lstReg = new QuestionnaireAnswerBO();
				EFrailTestAnswer eReg = EFrailTestAnswer.getById(dto.getAnswerId());
				lstReg.setAnswerId(eReg.getAnswerId());
				lstReg.setValue(eReg.getValue());
				lstReg.setQuestionId(eReg.getQuestionId());
				reg.getAnswers().add(lstReg);
			}
		}
		return reg;
	}
}
