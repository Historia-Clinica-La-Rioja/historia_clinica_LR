package net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest;

import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.documentlist.application.GetProfessionalInvolvedDocumentList;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureDocumentListFilterBo;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;
import net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.dto.ElectronicJointSignatureInvolvedDocumentListFilterDto;
import net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.dto.ElectronicSignatureInvolvedDocumentDto;

import net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.mapper.ElectronicSignatureInvolvedDocumentMapper;

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
@RequiredArgsConstructor
@Tag(name = "Get joint signature professional involved documents", description = "This controller is designed for obtaining documents that the professional can electronically sign")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR, ENFERMERO, ABORDAJE_VIOLENCIAS')")
@RequestMapping("/institution/{institutionId}/electronic-joint-signature/get-involved-document-list")
@RestController
public class ProfessionalInvolvedDocumentListController {

	private final ObjectMapper objectMapper;

	private final GetProfessionalInvolvedDocumentList getProfessionalInvolvedDocumentList;

	private final ElectronicSignatureInvolvedDocumentMapper electronicSignatureInvolvedDocumentMapper;

	@GetMapping
	public PageDto<ElectronicSignatureInvolvedDocumentDto> run(@PathVariable("institutionId") Integer institutionId,
															   @RequestParam(name = "pageNumber") Integer pageNumber,
															   @RequestParam(name = "pageSize") Integer pageSize,
															   @RequestParam(name = "filter", required = false) String stringFilter) throws JsonProcessingException {
		log.debug("Input parameters -> institutionId {}, pageNumber {}, pageSize {}, stringFilter {}", institutionId, pageNumber, pageSize, stringFilter);
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		ElectronicJointSignatureInvolvedDocumentListFilterDto filterDto = objectMapper.readValue(stringFilter, ElectronicJointSignatureInvolvedDocumentListFilterDto.class);
		ElectronicSignatureDocumentListFilterBo filter = electronicSignatureInvolvedDocumentMapper.fromElectronicJointSignatureInvolvedDocumentListFilterDto(filterDto, institutionId);
		Page<ElectronicSignatureInvolvedDocumentBo> electronicSignatureInvolvedDocumentBoList = getProfessionalInvolvedDocumentList.run(filter, pageable);
		Page<ElectronicSignatureInvolvedDocumentDto> result = electronicSignatureInvolvedDocumentBoList.map(electronicSignatureInvolvedDocumentMapper::toElectronicSignatureInvolvedDocumentDto);
		log.debug("Output -> {}", result);
		return PageDto.fromPage(result);
	}

}
