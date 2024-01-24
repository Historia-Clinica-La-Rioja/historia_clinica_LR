package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.PreMedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.PreMedicationDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface PreMedicationMapper {

    @Named(value = "toPreMedicationBo")
    @Mapping(target = "dosage.startDate", source = "dosage.startDateTime")
    PreMedicationBo toPreMedicationBo(PreMedicationDto preMedicationDto);

    @Named(value = "toPreMedicationDto")
    @Mapping(target = "dosage.startDateTime", source = "dosage.startDate")
    PreMedicationDto toPreMedicationDto(PreMedicationBo preMedicationBo);

    @Named("toListPreMedicationBo")
    @IterableMapping(qualifiedByName = "toPreMedicationBo")
    List<PreMedicationBo> toListPreMedicationBo(List<PreMedicationDto> preMedicationDtos);

    @Named("toListPreMedicationDto")
    @IterableMapping(qualifiedByName = "toPreMedicationDto")
    List<PreMedicationDto> toListPreMedicationDto(List<PreMedicationBo> preMedicationBos);

}
