package net.pladema.edMonton.create.controller;

import net.pladema.edMonton.create.controller.dto.CreateEdMontonDto;
import net.pladema.edMonton.create.controller.dto.EdMontonAnswerDto;
import net.pladema.edMonton.create.controller.service.CreateEdMontonService;

import net.pladema.edMonton.create.controller.service.domain.EdMontonAnswerBo;
import net.pladema.edMonton.create.controller.service.domain.EdMontonBo;

import net.pladema.edMonton.create.service.domain.EedMontonTestAnswer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class CreateEdMontonController implements CreateEdMontonAPI{

	private static final Logger LOG = LoggerFactory.getLogger(CreateEdMontonController.class);

	private CreateEdMontonService createEdMontonService;

	public CreateEdMontonController(CreateEdMontonService createEdMontonService){
		this.createEdMontonService = createEdMontonService;
	}


	@Override
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> createPatientEdMonton(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId,
			CreateEdMontonDto createEdMontonDto)
			throws IOException {

//		validateDto(createEdMontonDto);

		EdMontonBo edMontonBo = createEdMontonDto(patientId, createEdMontonDto);

		createEdMontonService.execute(edMontonBo);

		return ResponseEntity.ok().body(true);
	}



	private EdMontonBo createEdMontonDto(Integer patientId, CreateEdMontonDto createEdMontonDto) {
			EdMontonBo reg = new EdMontonBo();
			EdMontonAnswerBo lstReg;
			reg.setPatientId(patientId);
			reg.setResult(0);
			if (createEdMontonDto.getEdMonton() != null && createEdMontonDto.getEdMonton().size()>0) {
				reg.setAnswers(new ArrayList <EdMontonAnswerBo>() );
				for(EdMontonAnswerDto dto : createEdMontonDto.getEdMonton()) {
					lstReg = new EdMontonAnswerBo();
					EedMontonTestAnswer eReg = EedMontonTestAnswer.getById(dto.getAnswerId());
					lstReg.setAnswerId(eReg.getAnswerId());
					lstReg.setQuestionId(eReg.getQuestionId());
					lstReg.setValue(eReg.getValue());

					reg.getAnswers().add(lstReg);
					reg.setResult((reg.getResult() + eReg.getValue()));
				}
			}
			return reg;
	}
}
