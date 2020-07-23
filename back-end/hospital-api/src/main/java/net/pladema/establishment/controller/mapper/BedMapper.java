package net.pladema.establishment.controller.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.dto.PatientBedRelocationDto;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;
import net.pladema.sgx.dates.configuration.LocalDateMapper;

@Mapper(uses = {LocalDateMapper.class})
public interface BedMapper {

    @Named("toBedDto")
    BedDto toBedDto(Bed bed);

    @Named("toListBedDto")
    @IterableMapping(qualifiedByName = "toBedDto")
    List<BedDto> toListBedDto(List<Bed> bedList);

    @Named("toPatientBedRelocationDto")
    PatientBedRelocationDto toPatientBedRelocationDto(HistoricPatientBedRelocation historicPatientBedRelocation);
    
    @Named("fromPatientBedRelocationDto")
    HistoricPatientBedRelocation fromPatientBedRelocationDto(PatientBedRelocationDto patientBedRelocationDto);
    
}
