package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper()
public interface StudyMapper {
    @Named("parseTo")
    @IterableMapping(qualifiedByName = "toStudyBo")
    List<DiagnosticReportBo> parseTo(List<PrescriptionItemDto> studyDtoList);

    @Named("parseTo")
    @Mapping(target = "sctidCode", source = "snomed.id")
    DiagnosticReportBo parseTo(PrescriptionItemDto studyDto);
}
