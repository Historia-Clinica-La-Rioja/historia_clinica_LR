package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentHealthcareProfessionalDto;

import java.util.List;

@Mapper
public interface DocumentHealthcareProfessionalMapper {

	@Mapping(target = "professionType", source = "profession.type")
	@Mapping(target = "otherProfessionTypeDescription", source = "profession.otherTypeDescription")
	@Named("toDocumentHealthcareProfessionalDocumentBo")
	DocumentHealthcareProfessionalBo toDocumentHealthcareProfessionalDocumentBo(DocumentHealthcareProfessionalDto professionalDocumentDto);

	@Named("toDocumentHealthcareProfessionalBoList")
	@IterableMapping(qualifiedByName = "toDocumentHealthcareProfessionalDocumentBo")
	List<DocumentHealthcareProfessionalBo> toDocumentHealthcareProfessionalBoList(List<DocumentHealthcareProfessionalDto> professionalDocumentDtos);

	@Mapping(target = "profession.type", source = "professionType")
	@Mapping(target = "profession.otherTypeDescription", source = "otherProfessionTypeDescription")
	@Named("toDocumentHealthcareProfessionalDocumentDto")
	DocumentHealthcareProfessionalDto toDocumentHealthcareProfessionalDocumentDto(DocumentHealthcareProfessionalBo professionalBo);

	@Named("toDocumentHealthcareProfessionalDtoList")
	@IterableMapping(qualifiedByName = "toDocumentHealthcareProfessionalDocumentDto")
	List<DocumentHealthcareProfessionalDto> toDocumentHealthcareProfessionalDtoList(List<DocumentHealthcareProfessionalBo> professionalBos);

}
