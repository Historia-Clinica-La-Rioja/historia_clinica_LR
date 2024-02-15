package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentTypeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentResponseDto;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

@Mapper
public interface EpisodeDocumentDtoMapper {

	@Named("EpisodeDocumentBoToEpisodeDocumentDto")
	EpisodeDocumentResponseDto EpisodeDocumentBoToEpisodeDocumentDto(EpisodeDocumentResponseBo bo);

	@Named("DocumentTypeBoToDocumentTypeDto")
	DocumentTypeDto DocumentTypeBoToDocumentTypeDto(DocumentTypeBo bo);

}
