package net.pladema.electronicjointsignature.signdocument.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.signdocument.application.SignDocumentElectronicJointSignature;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Tag(name = "RejectElectronicJointSignatureController", description = "This controller is designed to sign a professional's electronic signature on a document")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR, ENFERMERO, ABORDAJE_VIOLENCIAS')")
@RequestMapping("/institution/{institutionId}/electronic-joint-signature/sign")
@RestController
public class SignDocumentElectronicJointSignatureController {

	private SignDocumentElectronicJointSignature signDocumentElectronicJointSignature;

	@PutMapping
	public Integer run(@PathVariable("institutionId") Integer institutionId, @RequestBody List<Long> documentIds) {
		log.debug("Input parameters -> institutionId {}, documentIds {}", institutionId, documentIds);
		Integer result = signDocumentElectronicJointSignature.run(documentIds);
		log.debug("Output -> {}", result);
		return result;
	}

}
