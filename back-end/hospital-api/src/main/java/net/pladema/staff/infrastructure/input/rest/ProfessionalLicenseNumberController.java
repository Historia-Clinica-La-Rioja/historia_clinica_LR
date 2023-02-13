package net.pladema.staff.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.sisa.refeps.controller.RefepsExternalService;
import net.pladema.sisa.refeps.controller.dto.ValidatedLicenseNumberDto;
import net.pladema.sisa.refeps.controller.mapper.ValidatedLicenceNumberMapper;
import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.staff.application.deleteprofessionallicensenumber.DeleteProfessionalLicenseNumber;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.application.saveprofessionallicensesnumber.SaveProfessionalLicensesNumber;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;

import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institution/{institutionId}/professional-license-number/healthcareprofessional/{healthcareprofessionalId}")
@Slf4j
@RequiredArgsConstructor
public class ProfessionalLicenseNumberController {

	private final GetLicenseNumberByProfessional getLicenseNumberByProfessional;

	private final SaveProfessionalLicensesNumber saveProfessionalLicensesNumber;

	private final DeleteProfessionalLicenseNumber deleteProfessionalLicenseNumber;

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final RefepsExternalService refepsExternalService;

	private final ValidatedLicenceNumberMapper validatedLicenceNumberMapper;

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

	@PostMapping("/validate")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ValidatedLicenseNumberDto> validateLicenseNumbers(@PathVariable(name = "institutionId") Integer institutionId,
																  @PathVariable(name = "healthcareprofessionalId") Integer healthcareProfessionalId,
																  @RequestBody List<String> licenseNumbers) {
		log.debug("Input parameters -> healthcareProfessionalId: {}, licenseNumbers {}", healthcareProfessionalId, licenseNumbers);
		HealthcareProfessionalBo currentProfessional = healthcareProfessionalService.findActiveProfessionalById(healthcareProfessionalId);
		List<ValidatedLicenseNumberBo> validatedLicenceNumbers = new ArrayList<>();
		try {
			validatedLicenceNumbers = refepsExternalService.validateLicenseNumber(currentProfessional.getIdentificationNumber(), licenseNumbers);
		} catch (Exception e) {
			log.error("Fallo en la comunicación => {}", e.getMessage());
			throw new RuntimeException(e);
		}
		return validatedLicenceNumberMapper.toListValidatedLicenseNumberDto(validatedLicenceNumbers);
	}

	@PostMapping("/delete")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable(name = "institutionId") Integer institutionId,
												  @PathVariable(name = "healthcareprofessionalId") Integer healthcareProfessionalId,
												  @RequestBody ProfessionalLicenseNumberDto professionalLicenseNumberDto) {
		log.debug("Input parameters -> licenseNumber {}", professionalLicenseNumberDto);
		deleteProfessionalLicenseNumber.run(this.mapToBo(professionalLicenseNumberDto));
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
