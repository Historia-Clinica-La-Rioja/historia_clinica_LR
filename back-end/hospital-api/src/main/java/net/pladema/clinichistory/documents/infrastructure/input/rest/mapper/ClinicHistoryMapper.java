package net.pladema.clinichistory.documents.infrastructure.input.rest.mapper;

import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;
import net.pladema.clinichistory.documents.infrastructure.input.rest.dto.CHDocumentSummaryDto;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface ClinicHistoryMapper {

	@Named("toDocumentSummaryDto")
	CHDocumentSummaryDto toDocumentSummaryDto (CHDocumentSummaryBo documentSummaryBo);

}
