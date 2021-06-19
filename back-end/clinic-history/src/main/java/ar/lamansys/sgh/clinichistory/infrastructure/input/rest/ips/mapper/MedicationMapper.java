package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.MedicationDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface MedicationMapper {

    @Named("toMedicationDto")
    MedicationDto toMedicationDto(MedicationBo medicationBo);

}
