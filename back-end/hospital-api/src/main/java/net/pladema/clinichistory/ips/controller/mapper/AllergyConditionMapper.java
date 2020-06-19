package net.pladema.clinichistory.ips.controller.mapper;

import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.ips.controller.dto.AllergyConditionDto;
import net.pladema.clinichistory.ips.repository.generalstate.AllergyConditionVo;
import net.pladema.clinichistory.ips.service.domain.AllergyConditionBo;
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
