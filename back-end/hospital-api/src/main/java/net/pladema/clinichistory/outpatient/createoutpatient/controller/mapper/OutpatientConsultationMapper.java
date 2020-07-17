package net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface OutpatientConsultationMapper {

    @Named("fromCreateOutpatientDto")
    OutpatientDocumentBo fromCreateOutpatientDto(CreateOutpatientDto createOutpatientDto);
}
