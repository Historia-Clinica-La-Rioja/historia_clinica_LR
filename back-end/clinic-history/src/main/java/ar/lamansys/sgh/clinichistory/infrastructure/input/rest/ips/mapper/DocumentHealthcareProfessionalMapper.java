package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentHealthcareProfessionalDto;

import java.util.List;

@Mapper
public interface DocumentHealthcareProfessionalMapper {

	@Named("toDocumentHealthcareProfessionalDocumentBo")
	DocumentHealthcareProfessionalBo toDocumentHealthcareProfessionalDocumentBo(DocumentHealthcareProfessionalDto professionalDocumentDto);

	@Named("toDocumentHealthcareProfessionalBoList")
	@IterableMapping(qualifiedByName = "toDocumentHealthcareProfessionalDocumentBo")
	List<DocumentHealthcareProfessionalBo> toDocumentHealthcareProfessionalBoList(List<DocumentHealthcareProfessionalDto> professionalDocumentDtos);


}
