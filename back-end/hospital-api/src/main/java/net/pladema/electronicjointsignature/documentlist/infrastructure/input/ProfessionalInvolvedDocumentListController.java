package net.pladema.electronicjointsignature.documentlist.infrastructure.input;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.documentlist.application.GetProfessionalInvolvedDocumentList;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureDocumentListFilterBo;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;
import net.pladema.electronicjointsignature.documentlist.infrastructure.input.dto.ElectronicSignatureInvolvedDocumentDto;

import net.pladema.electronicjointsignature.documentlist.infrastructure.input.mapper.ElectronicSignatureInvolvedDocumentMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Tag(name = "Get joint signature professional involved documents", description = "This controller is designed for obtaining documents that the professional can electronically sign")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR, ENFERMERO, ABORDAJE_VIOLENCIAS')")
@RequestMapping("/institution/{institutionId}/electronic-joint-signature/get-involved-document-list")
@RestController
public class ProfessionalInvolvedDocumentListController {

	private GetProfessionalInvolvedDocumentList getProfessionalInvolvedDocumentList;

	private ElectronicSignatureInvolvedDocumentMapper electronicSignatureInvolvedDocumentMapper;

	@GetMapping
	public PageDto<ElectronicSignatureInvolvedDocumentDto> run(@PathVariable("institutionId") Integer institutionId,
															   @RequestParam(name = "pageNumber") Integer pageNumber,
															   @RequestParam(name = "pageSize") Integer pageSize,
															   @RequestParam(name = "filter", required = false) String signatureStatusFilter) {
		log.debug("Input parameters -> institutionId {}, pageNumber {}, pageSize {}, signatureStatusFilter {}", institutionId, pageNumber, pageSize, signatureStatusFilter);
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		ElectronicSignatureDocumentListFilterBo filter = parseToFilter(institutionId, signatureStatusFilter);
		Page<ElectronicSignatureInvolvedDocumentBo> electronicSignatureInvolvedDocumentBoList = getProfessionalInvolvedDocumentList.run(filter, pageable);
		Page<ElectronicSignatureInvolvedDocumentDto> result = electronicSignatureInvolvedDocumentBoList.map(electronicSignatureInvolvedDocumentMapper::toElectronicSignatureInvolvedDocumentDto);
		log.debug("Output -> {}", result);
		return PageDto.fromPage(result);
	}

	private ElectronicSignatureDocumentListFilterBo parseToFilter(Integer institutionId, String signatureStatusFilter) {
		Short signatureStatusId = signatureStatusFilter != null ? EElectronicSignatureStatus.valueOf(signatureStatusFilter).getId() : null;
		return new ElectronicSignatureDocumentListFilterBo(institutionId, signatureStatusId);
	}

}
