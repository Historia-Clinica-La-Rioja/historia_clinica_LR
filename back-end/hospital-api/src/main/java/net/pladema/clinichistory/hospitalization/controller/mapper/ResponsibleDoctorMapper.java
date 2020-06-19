package net.pladema.clinichistory.hospitalization.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleDoctorDto;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ResponsibleDoctorMapper {

    @Named("toResponsibleDoctorDto")
    ResponsibleDoctorDto toResponsibleDoctorDto(ResponsibleDoctorBo responsibleDoctorBo);
}
