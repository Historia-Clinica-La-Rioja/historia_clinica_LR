package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
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
