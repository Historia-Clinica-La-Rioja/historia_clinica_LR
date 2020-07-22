package net.pladema.medicalconsultation.doctorsoffice.controller.mapper;

import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface DoctorsOfficeMapper {

    @Named("toDoctorsOfficeDto")
    DoctorsOfficeDto toDoctorsOfficeDto(DoctorsOfficeBo doctorsOfficeBo);

    @Named("toListDoctorsOfficeDto")
    @IterableMapping(qualifiedByName = "toDoctorsOfficeDto")
    List<DoctorsOfficeDto> toListDoctorsOfficeDto(List<DoctorsOfficeBo> doctorsOfficeBos);
}
