package net.pladema.staff.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professional-license-number/institution/{institutionId}")
@Slf4j
@RequiredArgsConstructor
public class ProfessionalLicenseNumberController {

	private final GetLicenseNumberByProfessional getLicenseNumberByProfessional;
	
	@GetMapping("/healthcareprofessional/{healthcareprofessionalId}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProfessionalLicenseNumberDto> getAllByProfessional(@PathVariable(name = "institutionId") Integer institutionId,
																   @PathVariable(name = "healthcareprofessionalId") Integer healthcareprofessionalId) {
		log.debug("Input parameters -> healthcareprofessionalId {}", healthcareprofessionalId);
		List<ProfessionalLicenseNumberDto> result = getLicenseNumberByProfessional.run(healthcareprofessionalId).stream().map(this::mapToDto).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	private ProfessionalLicenseNumberDto mapToDto(ProfessionalLicenseNumberBo bo){
		return new ProfessionalLicenseNumberDto(bo.getId(),
				bo.getLicenseNumber(),
				bo.getType().getId(),
				bo.getProfessionalProfessionId(),
				bo.getHealthcareProfessionalSpecialtyId());
	}

}
