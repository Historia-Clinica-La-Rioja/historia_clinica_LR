package net.pladema.staff.infrastructure.input.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.staff.application.getallprofessionalregistrationnumbers.GetAllProfessionalRegistrationNumbers;
import net.pladema.staff.controller.dto.LicenseNumberTypeDto;
import net.pladema.staff.controller.dto.ProfessionalRegistrationNumbersDto;
import net.pladema.staff.domain.ProfessionalRegistrationNumbersBo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.sisa.refeps.controller.RefepsExternalService;
import net.pladema.sisa.refeps.controller.dto.LicenseDataDto;
import net.pladema.sisa.refeps.controller.dto.ValidatedLicenseDataDto;
import net.pladema.staff.application.deleteprofessionallicensenumber.DeleteProfessionalLicenseNumber;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.application.saveprofessionallicensesnumber.SaveProfessionalLicensesNumber;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.service.HealthcareProfessionalService;
import ar.lamansys.sgh.shared.domain.ELicenseNumberTypeBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

@RestController
@RequestMapping("/institution/{institutionId}/professional-license-number/healthcareprofessional")
@Tag(name = "Professional License Numbers", description = "Professional License Numbers")
@Slf4j
@RequiredArgsConstructor
public class ProfessionalLicenseNumberController {

	private final GetLicenseNumberByProfessional getLicenseNumberByProfessional;

	private final SaveProfessionalLicensesNumber saveProfessionalLicensesNumber;

	private final DeleteProfessionalLicenseNumber deleteProfessionalLicenseNumber;

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final RefepsExternalService refepsExternalService;

	private final GetAllProfessionalRegistrationNumbers getAllProfessionalRegistrationNumbers;


	@GetMapping("/masterdata")
	public ResponseEntity<List<LicenseNumberTypeDto>> getLicenseNumberTypes(@PathVariable String institutionId) {
		log.debug("Input parameters -> {}", institutionId);
		List<LicenseNumberTypeDto> result = Stream.of(ELicenseNumberTypeBo.values())
				.map(tr-> new LicenseNumberTypeDto(tr.getId(), tr.getAcronym()))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/registration-numbers")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA, ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<ProfessionalRegistrationNumbersDto>> getAllProfessionalRegistrationNumbers(@PathVariable Integer institutionId) {
		log.debug("Input parameters -> {}", institutionId);
		List<ProfessionalRegistrationNumbersDto> result = getAllProfessionalRegistrationNumbers.run(institutionId).stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{healthcareprofessionalId}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProfessionalLicenseNumberDto> getAllByProfessional(@PathVariable(name = "institutionId") Integer institutionId,
																   @PathVariable(name = "healthcareprofessionalId") Integer healthcareprofessionalId) {
		log.debug("Input parameters -> healthcareprofessionalId {}", healthcareprofessionalId);
		List<ProfessionalLicenseNumberDto> result = getLicenseNumberByProfessional.run(healthcareprofessionalId).stream().map(this::mapToDto).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@PostMapping("/{healthcareprofessionalId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void save(@PathVariable(name = "institutionId") Integer institutionId,
					 @PathVariable(name = "healthcareprofessionalId") Integer healthcareProfessionalId,
					 @RequestBody List<ProfessionalLicenseNumberDto> professionalLicensesNumberDto) {
		log.debug("Input parameters -> professionalLicensesNumberDto {}", professionalLicensesNumberDto);
		saveProfessionalLicensesNumber.run(healthcareProfessionalId, professionalLicensesNumberDto.stream().map(this::mapToBo).collect(Collectors.toList()));
	}

	@PostMapping("/{healthcareprofessionalId}/validate")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ValidatedLicenseDataDto> validateLicenseNumbers(@PathVariable(name = "institutionId") Integer institutionId,
																  @PathVariable(name = "healthcareprofessionalId") Integer healthcareProfessionalId,
																  @RequestBody List<LicenseDataDto> licenseData) {
		log.debug("Input parameters -> healthcareProfessionalId: {}, licenseNumbers {}", healthcareProfessionalId, licenseData);
		HealthcareProfessionalBo currentProfessional = healthcareProfessionalService.findActiveProfessionalById(healthcareProfessionalId);
		List<ValidatedLicenseDataDto> validatedLicenceNumbers = new ArrayList<>();
		try {
			validatedLicenceNumbers = refepsExternalService.validateLicenseNumberAndType(healthcareProfessionalId, currentProfessional.getIdentificationNumber(), licenseData);
		} catch (Exception e) {
			log.error("Fallo en la comunicación => {}", e.getMessage());
			throw new RuntimeException(e);
		}
		return validatedLicenceNumbers;
	}

	@PostMapping("/{healthcareprofessionalId}/delete")
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

	private ProfessionalRegistrationNumbersDto mapToDto(ProfessionalRegistrationNumbersBo bo){
		return new ProfessionalRegistrationNumbersDto(
				bo.getHealthcareProfessionalId(),
				bo.getFirstName(),
				bo.getLastName(),
				bo.getNameSelfDetermination(),
				bo.getLicense()
						.stream()
						.map(this::mapToDto)
						.collect(Collectors.toList()));
	}

}
