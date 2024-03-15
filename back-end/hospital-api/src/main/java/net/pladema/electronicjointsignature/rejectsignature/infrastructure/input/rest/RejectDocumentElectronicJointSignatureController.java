package net.pladema.electronicjointsignature.rejectsignature.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.rejectsignature.application.RejectDocumentElectronicJointSignature;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectDocumentElectronicJointSignatureBo;

import net.pladema.electronicjointsignature.rejectsignature.infrastructure.input.mapper.RejectDocumentElectronicJointSignatureMapper;
import net.pladema.electronicjointsignature.rejectsignature.infrastructure.input.rest.dto.RejectDocumentElectronicJointSignatureDto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Tag(name = "RejectElectronicJointSignatureController", description = "This controller is designed to reject a professional's electronic signature on a document")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
@RequestMapping("/institution/{institutionId}/electronic-joint-signature/reject")
@RestController
public class RejectDocumentElectronicJointSignatureController {

	private RejectDocumentElectronicJointSignatureMapper rejectDocumentElectronicJointSignatureMapper;

	private RejectDocumentElectronicJointSignature rejectDocumentElectronicJointSignature;

	@PutMapping
	public Integer run(@PathVariable("institutionId") Integer institutionId, @RequestBody RejectDocumentElectronicJointSignatureDto rejectData) {
		log.debug("Input parameters -> institutionId {}, rejectData {}", institutionId, rejectData);
		RejectDocumentElectronicJointSignatureBo rejectDataBo = rejectDocumentElectronicJointSignatureMapper.fromRejectDocumentElectronicJointSignatureDto(rejectData);
		Integer result = rejectDocumentElectronicJointSignature.run(rejectDataBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
