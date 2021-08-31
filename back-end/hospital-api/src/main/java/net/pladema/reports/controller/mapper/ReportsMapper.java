package net.pladema.reports.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.reports.service.domain.AnnexIIBo;
import net.pladema.reports.service.domain.FormVBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface ReportsMapper {

    @Named("toAnexoIIDto")
    AnnexIIDto toAnexoIIDto(AnnexIIBo source);

    @Named("toFormVDto")
    FormVDto toFormVDto(FormVBo source);
}
