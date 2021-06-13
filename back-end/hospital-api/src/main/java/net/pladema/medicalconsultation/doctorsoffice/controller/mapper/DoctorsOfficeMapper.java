package net.pladema.medicalconsultation.doctorsoffice.controller.mapper;

import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = LocalDateMapper.class)
public interface DoctorsOfficeMapper {

    @Named("toDoctorsOfficeDto")
    DoctorsOfficeDto toDoctorsOfficeDto(DoctorsOfficeBo doctorsOfficeBo);

    @Named("toListDoctorsOfficeDto")
    @IterableMapping(qualifiedByName = "toDoctorsOfficeDto")
    List<DoctorsOfficeDto> toListDoctorsOfficeDto(List<DoctorsOfficeBo> doctorsOfficeBos);
}
