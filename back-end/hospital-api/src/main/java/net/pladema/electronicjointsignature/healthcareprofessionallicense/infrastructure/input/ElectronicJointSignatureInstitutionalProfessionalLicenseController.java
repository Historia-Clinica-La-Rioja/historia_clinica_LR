package net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.input;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.input.dto.ElectronicJointSignatureInstitutionProfessionalDto;
import net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.input.mapper.ElectronicJointSignatureInstitutionalProfessionalLicenseMapper;
import net.pladema.electronicjointsignature.healthcareprofessionallicense.application.FetchProfessionalLicenseData;
import net.pladema.electronicjointsignature.healthcareprofessionallicense.domain.ElectronicJointSignatureInstitutionProfessionalBo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Tag(name = "Joint signature professionals licenses", description = "This controller is meant to be used to obtain professionals license information")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
@RequestMapping("/institution/{institutionId}/electronic-joint-signature/get-current-institution-professionals")
@RestController
public class ElectronicJointSignatureInstitutionalProfessionalLicenseController {

	private ElectronicJointSignatureInstitutionalProfessionalLicenseMapper electronicJointSignatureInstitutionalProfessionalLicenseMapper;

	private FetchProfessionalLicenseData fetchProfessionalLicenseData;

	@GetMapping
	public List<ElectronicJointSignatureInstitutionProfessionalDto> run(@PathVariable("institutionId") Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		List<ElectronicJointSignatureInstitutionProfessionalBo> electronicJointSignatureInstitutionProfessionalBos = fetchProfessionalLicenseData.run(institutionId);
		List<ElectronicJointSignatureInstitutionProfessionalDto> result = electronicJointSignatureInstitutionalProfessionalLicenseMapper.toInstitutionProfessionalDtoList(electronicJointSignatureInstitutionProfessionalBos);
		log.debug("Output -> {}", result);
		return result;
	}

}
