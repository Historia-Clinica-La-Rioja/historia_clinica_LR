package net.pladema.establishment.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.establishment.infrastructure.input.rest.dto.PatientBedRelocationDto;
import net.pladema.establishment.domain.bed.BedRelocationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface BedRelocationMapper {

    @Named("toPatientBedRelocationDto")
    PatientBedRelocationDto toPatientBedRelocationDto(BedRelocationBo bedRelocationBo);

    @Named("fromPatientBedRelocationDto")
    BedRelocationBo fromPatientBedRelocationDto(PatientBedRelocationDto patientBedRelocationDto);
}