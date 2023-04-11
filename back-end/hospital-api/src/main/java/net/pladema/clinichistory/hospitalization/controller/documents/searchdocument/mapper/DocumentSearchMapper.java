package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.mapper;

import ar.lamansys.sgh.clinichistory.application.searchDocument.EmergencyCareEpisodeTriageSearchBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto.DocumentHistoricDto;
import ar.lamansys.sgh.clinichistory.application.searchDocument.DocumentHistoricBo;
import net.pladema.emergencycare.controller.document.documentsearch.dto.EmergencyCareEpisodeTriageSearchDto;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface DocumentSearchMapper {

    DocumentHistoricDto toDocumentHistoricDto(DocumentHistoricBo documentHistoricBo);

	EmergencyCareEpisodeTriageSearchDto toEmergencyCareEpisodeTriageSearchDto(EmergencyCareEpisodeTriageSearchBo emergencyCareEpisodeTriageSearchBo);

	List<EmergencyCareEpisodeTriageSearchDto> toEmergencyCareEpisodeTriageSearchListDto(List<EmergencyCareEpisodeTriageSearchBo> emergencyCareEpisodeTriageSearchListBo);

}
