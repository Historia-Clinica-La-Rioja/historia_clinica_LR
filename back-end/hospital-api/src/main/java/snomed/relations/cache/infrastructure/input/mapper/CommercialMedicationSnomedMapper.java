package snomed.relations.cache.infrastructure.input.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import org.mapstruct.Named;

import snomed.relations.cache.domain.GetCommercialMedicationSnomedBo;
import snomed.relations.cache.infrastructure.input.rest.dto.GetCommercialMedicationSnomedDto;

import java.util.List;

@Mapper
public interface CommercialMedicationSnomedMapper {

	@Named("toCommercialMedicationSnomedDto")
    GetCommercialMedicationSnomedDto toGetCommercialMedicationSnomedDto(GetCommercialMedicationSnomedBo getCommercialMedicationSnomedBo);

	@IterableMapping(qualifiedByName = "toCommercialMedicationSnomedDto")
	@Named("toCommercialMedicationSnomedDtoList")
	List<GetCommercialMedicationSnomedDto> toGetCommercialMedicationSnomedDtoList(List<GetCommercialMedicationSnomedBo> getCommercialMedicationSnomedBos);

}
