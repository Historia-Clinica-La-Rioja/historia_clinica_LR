package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.AnestheticReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface AnestheticReportMapper {
    @Named("fromAnestheticReportDto")
    AnestheticReportBo fromAnestheticReportDto(AnestheticReportDto anestheticReport);

    @Named("fromAnestheticReportBo")
    AnestheticReportDto fromAnestheticReportBo(AnestheticReportBo anestheticReport);
}
