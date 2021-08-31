package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {SnomedMapper.class})
public interface StudyMapper {
    @Named("parseToList")
    @IterableMapping(qualifiedByName = "parseTo")
    List<DiagnosticReportBo> parseToList(List<PrescriptionItemDto> studyDtoList);

    @Named("parseTo")
    DiagnosticReportBo parseTo(PrescriptionItemDto studyDto);
}
