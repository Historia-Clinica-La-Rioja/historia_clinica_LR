package ar.lamansys.sgh.publicapi.activities.staff.infrastructure.input.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.activities.staff.application.FetchHealthcareProfessionalsByInstitution;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/public-api/institution/refset/{refsetCode}/staff")
@Tag(name = "PublicApi Facturacion", description = "Public Api Institution Staff")

public class FetchHealthcareProfessionalsByInstitutionController {

	private FetchHealthcareProfessionalsByInstitution fetchHealthcareProfessionalsByInstitution;

	@GetMapping
	public @ResponseBody List<MedicineDoctorCompleteDto> getProfessionalsByInstitution(
			@PathVariable("refsetCode") String refsetCode) {
		log.debug("refsetCode {}", refsetCode);

		var result = fetchHealthcareProfessionalsByInstitution.run(refsetCode);

		log.debug("refsetCode {} => {}", refsetCode, result);

		return result;
	}
}
