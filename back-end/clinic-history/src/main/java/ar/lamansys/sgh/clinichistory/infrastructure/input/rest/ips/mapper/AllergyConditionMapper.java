package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AllergyConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReferableItemDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface AllergyConditionMapper {

    @Named("toAllergyConditionDto")
    AllergyConditionDto toAllergyConditionDto(AllergyConditionBo allergyConditionBo);

    @Named("toAllergyConditionBo")
    AllergyConditionBo toAllergyConditionBo(AllergyConditionDto allergyConditionDto);

    @Named("toListAllergyConditionBo")
    @IterableMapping(qualifiedByName = "toAllergyConditionBo")
    List<AllergyConditionBo> toListAllergyConditionBo(List<AllergyConditionDto> allergyConditionDto);

	@Named("toListAllergyConditionDto")
	@IterableMapping(qualifiedByName = "toAllergyConditionDto")
	List<AllergyConditionDto> toListAllergyConditionDto(List<AllergyConditionBo> allergyConditionBo);

	@Named("toAllergyConditionReferableItemBo")
	ReferableItemBo<AllergyConditionBo> toAllergyConditionReferableItemBo(ReferableItemDto<AllergyConditionDto> referableItemDto);

	@Named("toAllergyConditionReferableItemDto")
	ReferableItemDto<AllergyConditionDto> toAllergyConditionReferableItemDto(ReferableItemBo<AllergyConditionBo> referableItemBo);

}
