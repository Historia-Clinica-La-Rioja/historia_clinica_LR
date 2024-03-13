package net.pladema.electronicjointsignature.professionalsstatus.infrastructure.input.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import jdk.jfr.Name;
import net.pladema.electronicjointsignature.professionalsstatus.domain.DocumentElectronicSignatureProfessionalStatusBo;
import net.pladema.electronicjointsignature.professionalsstatus.infrastructure.input.dto.DocumentElectronicSignatureProfessionalStatusDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, EElectronicSignatureStatus.class})
public interface DocumentElectronicSignatureProfessionalStatusMapper {

	@Mapping(target = "status", source = "statusId")
	@Named("toDocumentElectronicSignatureProfessionalStatusDto")
	DocumentElectronicSignatureProfessionalStatusDto toDocumentElectronicSignatureProfessionalStatusDto (DocumentElectronicSignatureProfessionalStatusBo documentElectronicSignatureProfessionalStatusBo);

	@IterableMapping(qualifiedByName = "toDocumentElectronicSignatureProfessionalStatusDto")
	@Name("toDocumentElectronicSignatureProfessionalStatusDtoList")
	List<DocumentElectronicSignatureProfessionalStatusDto> toDocumentElectronicSignatureProfessionalStatusDtoList (List<DocumentElectronicSignatureProfessionalStatusBo> documentElectronicSignatureProfessionalStatusBoList);

}
