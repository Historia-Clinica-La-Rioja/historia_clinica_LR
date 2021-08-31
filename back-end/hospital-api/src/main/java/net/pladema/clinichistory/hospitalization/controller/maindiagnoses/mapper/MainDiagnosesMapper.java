package net.pladema.clinichistory.hospitalization.controller.maindiagnoses.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto.MainDiagnosisDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface MainDiagnosesMapper {

    @Named("fromMainDiagnoseDto")
    MainDiagnosisBo fromMainDiagnoseDto(MainDiagnosisDto mainDiagnoses);

}
