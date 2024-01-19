package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.HealthConditionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.AnestheticReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, HealthConditionMapper.class})
public interface AnestheticReportMapper {

    @Named("fromAnestheticReportDto")
    @Mapping(target = "mainDiagnosis", source = "mainDiagnosis", qualifiedByName = "toHealthConditionBoFromDiagnosisDto")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisBo")
    AnestheticReportBo fromAnestheticReportDto(AnestheticReportDto anestheticReport);

    @Named("fromAnestheticReportBo")
    @Mapping(target = "mainDiagnosis", source = "mainDiagnosis", qualifiedByName = "toDiagnosisDtoFromHealthConditionBo")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisDto")
    AnestheticReportDto fromAnestheticReportBo(AnestheticReportBo anestheticReport);
}
