package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.application.SaveInstitutionAddress;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.domain.SaveInstitutionAddressBo;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.infrastructure.input.mapper.SaveInstitutionAddressMapper;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.infrastructure.input.rest.dto.SaveInstitutionAddressDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/sanitary-responsibility-area/save-institution-address")
@RestController
public class SaveInstitutionAddressController {

	private final SaveInstitutionAddressMapper saveInstitutionAddressMapper;

	private final SaveInstitutionAddress saveInstitutionAddress;

	@PostMapping
	public Integer run(@PathVariable("institutionId") Integer institutionId, @RequestBody SaveInstitutionAddressDto institutionAddressDto) {
		log.debug("Input parameters -> institutionId {}, institutionAddressDto {}", institutionId, institutionAddressDto);
		SaveInstitutionAddressBo institutionAddressAndCoordinatesBo = saveInstitutionAddressMapper.toSaveInstitutionAddressAndGlobalCoordinatesBo(institutionAddressDto, institutionId);
		Integer result = saveInstitutionAddress.run(institutionAddressAndCoordinatesBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
