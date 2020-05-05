package net.pladema.internation.controller.ips.mapper;

import net.pladema.internation.controller.ips.dto.MedicationDto;
import net.pladema.internation.service.ips.domain.MedicationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface MedicationMapper {

    @Named("toMedicationDto")
    MedicationDto toMedicationDto(MedicationBo medicationBo);

}
