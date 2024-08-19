package ar.lamansys.sgh.publicapi.activities.medicalcoverages.infrastructure.input.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.FetchAllMedicalCoverages;
import ar.lamansys.sgh.shared.infrastructure.input.service.MedicalCoverageDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RequestMapping("/public-api/medical-coverages")
@Tag(name = "PublicApi Facturacion", description = "Public Api Medical Coverages")
@RestController
public class FetchAllMedicalCoveragesController {

	private FetchAllMedicalCoverages fetchAllMedicalCoverages;

	@GetMapping("/all")
	public @ResponseBody List<MedicalCoverageDataDto> getAllMedicalCoverages() {
		log.debug("Get all medical coverages");

		var result = fetchAllMedicalCoverages.run();

		log.debug("result => {}", result);

		return result;
	}
}
