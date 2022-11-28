package net.pladema.staff.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.application.saveprofessionallicensesnumber.SaveProfessionalLicensesNumber;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;

import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institution/{institutionId}/professional-license-number/healthcareprofessional/{healthcareprofessionalId}")
@Slf4j
@RequiredArgsConstructor
public class ProfessionalLicenseNumberController {

	private final GetLicenseNumberByProfessional getLicenseNumberByProfessional;

	private final SaveProfessionalLicensesNumber saveProfessionalLicensesNumber;

	@GetMapping()
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProfessionalLicenseNumberDto> getAllByProfessional(@PathVariable(name = "institutionId") Integer institutionId,
																   @PathVariable(name = "healthcareprofessionalId") Integer healthcareprofessionalId) {
		log.debug("Input parameters -> healthcareprofessionalId {}", healthcareprofessionalId);
		List<ProfessionalLicenseNumberDto> result = getLicenseNumberByProfessional.run(healthcareprofessionalId).stream().map(this::mapToDto).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@PostMapping()
	@ResponseStatus(code = HttpStatus.OK)
	public void save(@PathVariable(name = "institutionId") Integer institutionId,
					 @PathVariable(name = "healthcareprofessionalId") Integer healthcareProfessionalId,
					 @RequestBody List<ProfessionalLicenseNumberDto> professionalLicensesNumberDto) {
		log.debug("Input parameters -> professionalLicensesNumberDto {}", professionalLicensesNumberDto);
		saveProfessionalLicensesNumber.run(healthcareProfessionalId, professionalLicensesNumberDto.stream().map(this::mapToBo).collect(Collectors.toList()));
	}

	private ProfessionalLicenseNumberBo mapToBo(ProfessionalLicenseNumberDto dto){
		return new ProfessionalLicenseNumberBo(dto.getId(),
				dto.getLicenseNumber(),
				ELicenseNumberTypeBo.map(dto.getTypeId()),
				dto.getHealthcareProfessionalSpecialtyId()==null?dto.getProfessionalProfessionId() : null,
				dto.getHealthcareProfessionalSpecialtyId());
	}

	private ProfessionalLicenseNumberDto mapToDto(ProfessionalLicenseNumberBo bo){
		return new ProfessionalLicenseNumberDto(bo.getId(),
				bo.getLicenseNumber(),
				bo.getType().getId(),
				bo.getProfessionalProfessionId(),
				bo.getHealthcareProfessionalSpecialtyId());
	}

}
