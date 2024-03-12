package net.pladema.electronicjointsignature.documentlist.infrastructure.input;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.documentlist.application.GetProfessionalInvolvedDocumentList;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;
import net.pladema.electronicjointsignature.documentlist.infrastructure.input.dto.ElectronicSignatureInvolvedDocumentDto;

import net.pladema.electronicjointsignature.documentlist.infrastructure.input.mapper.ElectronicSignatureInvolvedDocumentMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Tag(name = "Get joint signature professional involved documents", description = "This controller is designed for obtaining documents that the professional can electronically sign")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
@RequestMapping("/institution/{institutionId}/electronic-joint-signature/get-involved-document-list")
@RestController
public class ProfessionalInvolvedDocumentListController {

	private GetProfessionalInvolvedDocumentList getProfessionalInvolvedDocumentList;

	private ElectronicSignatureInvolvedDocumentMapper electronicSignatureInvolvedDocumentMapper;

	@GetMapping
	public List<ElectronicSignatureInvolvedDocumentDto> run(@PathVariable("institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<ElectronicSignatureInvolvedDocumentBo> electronicSignatureInvolvedDocumentBoList = getProfessionalInvolvedDocumentList.run(institutionId);
		List<ElectronicSignatureInvolvedDocumentDto> result = electronicSignatureInvolvedDocumentMapper.toElectronicSignatureInvolvedDocumentDtoList(electronicSignatureInvolvedDocumentBoList);
		log.debug("Output -> {}", result);
		return result;
	}

}
