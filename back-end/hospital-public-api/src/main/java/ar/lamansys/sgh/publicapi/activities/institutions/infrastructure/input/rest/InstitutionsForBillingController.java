package ar.lamansys.sgh.publicapi.activities.institutions.infrastructure.input.rest;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.activities.institutions.application.FetchInstitutionsForBilling;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/public-api/activities/institutions")
@Tag(name = "PublicApi Facturacion", description = "Public Api Institutions for Billing")
public class InstitutionsForBillingController {

	FetchInstitutionsForBilling fetchInstitutions;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<InstitutionInfoDto> getInstitutionsForBilling() {
		log.debug("Fetching all institutions for billing");
		var result = fetchInstitutions.fetch();
		log.debug("result => {}", result);
		return result;
	}
}
