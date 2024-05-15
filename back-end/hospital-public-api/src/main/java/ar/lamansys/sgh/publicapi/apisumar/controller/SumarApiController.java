package ar.lamansys.sgh.publicapi.apisumar.controller;

import ar.lamansys.sgh.publicapi.apisumar.repository.model.ConsultationDetailData;
import ar.lamansys.sgh.publicapi.apisumar.service.SumarApiService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "PublicApi Sumar", description = "Public Api Sumar Access")
@RequestMapping("/public-api/sumar/consultations-detail")
public class SumarApiController {

	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final SumarApiService sumarApiService;

	public SumarApiController(SumarApiService sumarApiService) {
		this.sumarApiService = sumarApiService;
	}

	@GetMapping
	public @ResponseBody List<ConsultationDetailData> getAllConsultationDetailData() throws Exception {
		return sumarApiService.getAllConsultationsDetailData();
	}
}
