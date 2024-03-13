package net.pladema.electronicjointsignature.professionalsstatus.infrastructure.input;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.electronicjointsignature.professionalsstatus.application.GetDocumentElectronicSignatureProfessionalStatus;
import net.pladema.electronicjointsignature.professionalsstatus.domain.DocumentElectronicSignatureProfessionalStatusBo;
import net.pladema.electronicjointsignature.professionalsstatus.infrastructure.input.dto.DocumentElectronicSignatureProfessionalStatusDto;

import net.pladema.electronicjointsignature.professionalsstatus.infrastructure.input.mapper.DocumentElectronicSignatureProfessionalStatusMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Tag(name = "Get document joint signature professional status", description = "This controller is designed for obtaining the professional's signature status of a document")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
@RequestMapping("/institution/{institutionId}/document/{documentId}/electronic-joint-signature/get-professionals-status")
@RestController
public class GetDocumentElectronicSignatureProfessionalStatusController {

	private GetDocumentElectronicSignatureProfessionalStatus getDocumentElectronicSignatureProfessionalStatus;

	private DocumentElectronicSignatureProfessionalStatusMapper documentElectronicSignatureProfessionalStatusMapper;

	@GetMapping
	public List<DocumentElectronicSignatureProfessionalStatusDto> run(@PathVariable("institutionId") Integer institutionId, @PathVariable("documentId") Long documentId) {
		log.debug("Input parameters -> institutionId {}, documentId {}", institutionId, documentId);
		List<DocumentElectronicSignatureProfessionalStatusBo> status = getDocumentElectronicSignatureProfessionalStatus.run(documentId);
		List<DocumentElectronicSignatureProfessionalStatusDto> result = documentElectronicSignatureProfessionalStatusMapper.toDocumentElectronicSignatureProfessionalStatusDtoList(status);
		log.debug("Output -> {}", result);
		return result;
	}

}
