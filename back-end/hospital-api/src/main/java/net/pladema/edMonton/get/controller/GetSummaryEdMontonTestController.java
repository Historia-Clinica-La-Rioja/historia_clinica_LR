package net.pladema.edMonton.get.controller;

import net.pladema.edMonton.get.controller.dto.EdMontonSummary;

import net.pladema.edMonton.get.service.GetEdMontonService;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class GetSummaryEdMontonTestController implements GetSummaryEdMontonTest{
	
	private final GetEdMontonService getEdMontonService;

	public GetSummaryEdMontonTestController(GetEdMontonService getEdMontonService) {
		this.getEdMontonService = getEdMontonService;
	}

	@Override
	public ResponseEntity<EdMontonSummary> getPatientConsultationEdMontonTest
			(Integer institutionId,
			 Integer patientId,
			 Integer edMonton) throws IOException {

		EdMontonSummary edMontonSummary = new EdMontonSummary();

		EdMontonSummary result = getEdMontonService.findEdMontonSummary(edMonton);

		edMontonSummary.setTittle("Test EdMonton");
		edMontonSummary.setNameProfessional(result.getNameProfessional());
		edMontonSummary.setMiddleNameProfessional(result.getMiddleNameProfessional());
		edMontonSummary.setLastNameProfessional(result.getLastNameProfessional());
		edMontonSummary.setCreatedOn(result.getCreatedOn());
		edMontonSummary.setLicenseNumber(result.getLicenseNumber());
		
		return ResponseEntity.ok(edMontonSummary);
		
	}
}
