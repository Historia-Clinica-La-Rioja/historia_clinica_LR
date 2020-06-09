package net.pladema.internation.controller.ips.mapper;

import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.ips.dto.AllergyConditionDto;
import net.pladema.internation.repository.ips.generalstate.AllergyConditionVo;
import net.pladema.internation.service.ips.domain.AllergyConditionBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface AllergyConditionMapper {

    @Named("toAllergyConditionDto")
    @Mapping(target = "severity", source = "severityId")
    AllergyConditionDto toAllergyConditionDto(AllergyConditionBo allergyConditionBo);

    @Named("toAllergyConditionBo")
    AllergyConditionBo toAllergyConditionBo(AllergyConditionVo allergyConditionVo);

    @Named("toListAllergyConditionBo")
    @IterableMapping(qualifiedByName = "toAllergyConditionBo")
    List<AllergyConditionBo> toListAllergyConditionBo(List<AllergyConditionVo> allergyConditionVoList);
}
