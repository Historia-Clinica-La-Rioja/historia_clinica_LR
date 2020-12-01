package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.MedicationDto;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface MedicationMapper {

    @Named("toMedicationDto")
    MedicationDto toMedicationDto(MedicationBo medicationBo);

}
