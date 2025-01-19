package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.application.GetSanitaryResponsibilityAreaInstitutionAddress;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.domain.GetSanitaryResponsibilityAreaInstitutionAddressBo;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.infrastructure.input.mapper.GetSanitaryResponsibilityAreaInstitutionAddressMapper;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.infrastructure.input.rest.dto.GetSanitaryResponsibilityAreaInstitutionAddressDto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/sanitary-responsibility-area/get-institution-address")
@RestController
public class GetSanitaryResponsibilityAreaInstitutionAddressController {

	private final GetSanitaryResponsibilityAreaInstitutionAddress getSanitaryResponsibilityAreaInstitutionAddress;

	private final GetSanitaryResponsibilityAreaInstitutionAddressMapper getSanitaryResponsibilityAreaInstitutionAddressMapper;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public GetSanitaryResponsibilityAreaInstitutionAddressDto run(@PathVariable("institutionId") Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		GetSanitaryResponsibilityAreaInstitutionAddressBo resultBo = getSanitaryResponsibilityAreaInstitutionAddress.run(institutionId);
		GetSanitaryResponsibilityAreaInstitutionAddressDto result = getSanitaryResponsibilityAreaInstitutionAddressMapper.fromGetSanitaryResponsibilityAreaInstitutionAddressBo(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
