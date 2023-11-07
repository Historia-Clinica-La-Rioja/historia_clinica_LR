package net.pladema.questionnaires.familybg.create.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.questionnaires.common.domain.model.QuestionnaireAnswerBO;
import net.pladema.questionnaires.common.domain.model.QuestionnaireBO;
import net.pladema.questionnaires.common.dto.CreateQuestionnaireDTO;
import net.pladema.questionnaires.common.dto.QuestionnaireAnswerDTO;
import net.pladema.questionnaires.familybg.create.domain.EFamilyBgTestAnswer;
import net.pladema.questionnaires.familybg.create.domain.service.CreateFamilyBgService;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class CreateFamilyBgController implements CreateFamilyBgAPI {

	private static final Logger logger = LoggerFactory.getLogger(CreateFamilyBgController.class);

	private final CreateFamilyBgService createFamilyBgService;

	public CreateFamilyBgController(CreateFamilyBgService createFamilyBgService) {
		this.createFamilyBgService = createFamilyBgService;
	}

	@Override
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> createPatientFamilyBg(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "patientId") Integer patientId, @RequestBody CreateQuestionnaireDTO createFamilyBgDTO) {
		QuestionnaireBO familyBgBO = createFamilyBgDTO(patientId, createFamilyBgDTO);

		createFamilyBgService.execute(familyBgBO);

		logger.debug("Family background questionnaire created successfully.");

		return ResponseEntity.ok().body(true);
	}

	private QuestionnaireBO createFamilyBgDTO(Integer patientId, CreateQuestionnaireDTO createFamilyBgDTO) {
		QuestionnaireBO reg = new QuestionnaireBO();
		QuestionnaireAnswerBO lstReg;
		reg.setPatientId(patientId);
		if (createFamilyBgDTO.getQuestionnaire() != null && !createFamilyBgDTO.getQuestionnaire().isEmpty()) {
			reg.setAnswers(new ArrayList<>());
			for (QuestionnaireAnswerDTO dto : createFamilyBgDTO.getQuestionnaire()) {
				lstReg = new QuestionnaireAnswerBO();
				EFamilyBgTestAnswer eReg = EFamilyBgTestAnswer.getById(dto.getQuestionId());
				lstReg.setAnswerId(eReg.getAnswerId());
				lstReg.setQuestionId(eReg.getQuestionId());
				lstReg.setValue(Integer.valueOf(dto.getValue()));
				reg.getAnswers().add(lstReg);
			}
		}
		return reg;
	}

}
