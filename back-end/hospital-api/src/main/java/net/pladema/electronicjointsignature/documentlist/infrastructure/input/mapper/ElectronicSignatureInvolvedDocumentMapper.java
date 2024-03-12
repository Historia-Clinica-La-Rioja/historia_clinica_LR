package net.pladema.electronicjointsignature.documentlist.infrastructure.input.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;
import net.pladema.electronicjointsignature.documentlist.infrastructure.input.dto.ElectronicSignatureInvolvedDocumentDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, EElectronicSignatureStatus.class, EDocumentType.class})
public interface ElectronicSignatureInvolvedDocumentMapper {

	@Mapping(source = "signatureStatusId", target = "signatureStatus")
	@Mapping(source = "documentTypeId", target = "documentType")
	@Named("toElectronicSignatureInvolvedDocumentDto")
	ElectronicSignatureInvolvedDocumentDto toElectronicSignatureInvolvedDocumentDto(ElectronicSignatureInvolvedDocumentBo electronicSignatureInvolvedDocumentBo);

	@IterableMapping(qualifiedByName = "toElectronicSignatureInvolvedDocumentDto")
	@Named("toElectronicSignatureInvolvedDocumentDtoList")
	List<ElectronicSignatureInvolvedDocumentDto> toElectronicSignatureInvolvedDocumentDtoList(List<ElectronicSignatureInvolvedDocumentBo> electronicSignatureInvolvedDocumentBoList);


}
