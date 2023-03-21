package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.DocumentTypeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentResponseDto;

import net.pladema.clinichistory.hospitalization.controller.dto.StoredFileDto;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import net.pladema.clinichistory.hospitalization.service.domain.StoredFileBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface EpisodeDocumentDtoMapper {

	@Named("EpisodeDocumentBoToEpisodeDocumentDto")
	EpisodeDocumentResponseDto EpisodeDocumentBoToEpisodeDocumentDto(EpisodeDocumentResponseBo bo);

	@Named("DocumentTypeBoToDocumentTypeDto")
	DocumentTypeDto DocumentTypeBoToDocumentTypeDto(DocumentTypeBo bo);

	@Named("StoredFileBoToStoredFileDto")
	StoredFileDto StoredFileBoToStoredFileDto(StoredFileBo bo);
}
