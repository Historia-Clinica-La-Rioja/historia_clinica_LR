package net.pladema.clinichistory.ips.controller.mapper;

import net.pladema.clinichistory.ips.controller.dto.MedicationDto;
import net.pladema.clinichistory.ips.service.domain.MedicationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface MedicationMapper {

    @Named("toMedicationDto")
    MedicationDto toMedicationDto(MedicationBo medicationBo);

}
