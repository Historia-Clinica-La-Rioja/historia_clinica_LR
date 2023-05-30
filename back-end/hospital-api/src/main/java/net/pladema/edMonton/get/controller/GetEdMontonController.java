package net.pladema.edMonton.get.controller;

import net.pladema.edMonton.get.controller.dto.EdMontonAnswers;

import net.pladema.edMonton.get.service.GetEdMontonService;
import net.pladema.edMonton.repository.domain.Answer;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class GetEdMontonController implements GetEdMontonAPI {

	private GetEdMontonService getEdMontonService;

	public GetEdMontonController(GetEdMontonService getEdMontonService){this.getEdMontonService = getEdMontonService;}


	public ResponseEntity<List<EdMontonAnswers>> getPatientConsultationEdMontonTest
			(Integer institutionId,
			 Integer patientId) throws IOException {

		List<Answer> lst = getEdMontonService.findPatientEdMonton(patientId);

		return ResponseEntity.ok().body( createEdMontonDto( lst ));
	}

	private List<EdMontonAnswers> createEdMontonDto(List<Answer> lst){
		List<EdMontonAnswers> lstEdMonton = new ArrayList<>();
		EdMontonAnswers regDto;
		for(Answer qr : lst){
			regDto = new EdMontonAnswers();
			regDto.setIdQuestion(qr.getItemId());
			regDto.setIdAnswer(qr.getAnswerId());
			lstEdMonton.add(regDto);
			regDto.setId(qr.getId());
		}

		return lstEdMonton;
	}
}
