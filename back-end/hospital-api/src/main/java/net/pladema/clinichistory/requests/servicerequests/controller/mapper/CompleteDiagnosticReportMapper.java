package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.clinichistory.requests.servicerequests.controller.dto.CompleteRequestDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface CompleteDiagnosticReportMapper {

    @Named("parseTo")
    CompleteDiagnosticReportBo parseTo(CompleteRequestDto completeRequestDto);
}
