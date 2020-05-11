package net.pladema.internation.controller.internment.mapper;

import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;
import net.pladema.internation.service.internment.domain.ResponsibleDoctorBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ResponsibleDoctorMapper {

    @Named("toResponsibleDoctorDto")
    ResponsibleDoctorDto toResponsibleDoctorDto(ResponsibleDoctorBo responsibleDoctorBo);
}
