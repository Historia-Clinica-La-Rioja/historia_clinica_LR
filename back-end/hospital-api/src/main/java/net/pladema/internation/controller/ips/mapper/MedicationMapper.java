package net.pladema.internation.controller.ips.mapper;

import net.pladema.internation.controller.ips.dto.MedicationDto;
import net.pladema.internation.repository.ips.generalstate.MedicationVo;
import net.pladema.internation.service.ips.domain.MedicationBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface MedicationMapper {

    @Named("toMedicationDto")
    MedicationDto toMedicationDto(MedicationBo medicationBo);

    @Named("toMedicationBo")
    MedicationBo toMedicationBo(MedicationVo medicationVo);

    @Named("toListMedicationBo")
    @IterableMapping(qualifiedByName = "toMedicationBo")
    List<MedicationBo> toListMedicationBo(List<MedicationVo> medicationVo);

}
