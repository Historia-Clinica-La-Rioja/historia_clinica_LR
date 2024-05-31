package net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureDocumentListFilterBo;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;
import net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.dto.ElectronicJointSignatureInvolvedDocumentListFilterDto;
import net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.dto.ElectronicSignatureInvolvedDocumentDto;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {LocalDateMapper.class, EElectronicSignatureStatus.class})
public interface ElectronicSignatureInvolvedDocumentMapper {

	@Mapping(source = "signatureStatusId", target = "signatureStatus")
	@Named("toElectronicSignatureInvolvedDocumentDto")
	ElectronicSignatureInvolvedDocumentDto toElectronicSignatureInvolvedDocumentDto(ElectronicSignatureInvolvedDocumentBo electronicSignatureInvolvedDocumentBo);

	@Mapping(target = "signatureStatusIds", expression = "java(parseToEnumIds(electronicJointSignatureInvolvedDocumentListFilterDto.getElectronicSignaturesStatusIds()))")
	@Mapping(target = "institutionId", expression = "java(institutionId)")
	@Named("fromElectronicJointSignatureInvolvedDocumentListFilterDto")
	ElectronicSignatureDocumentListFilterBo fromElectronicJointSignatureInvolvedDocumentListFilterDto (ElectronicJointSignatureInvolvedDocumentListFilterDto electronicJointSignatureInvolvedDocumentListFilterDto,
																									   @Context Integer institutionId);

	default List<Short> parseToEnumIds(List<EElectronicSignatureStatus> eElectronicSignatureStatuses) {
		if (eElectronicSignatureStatuses != null)
			return eElectronicSignatureStatuses.stream().map(EElectronicSignatureStatus::getId).collect(Collectors.toList());
		return null;
	}

}
