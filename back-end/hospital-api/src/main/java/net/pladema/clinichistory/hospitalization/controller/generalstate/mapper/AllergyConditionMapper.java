package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.documents.service.ips.domain.AllergyConditionBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.AllergyConditionDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface AllergyConditionMapper {

    @Named("toAllergyConditionDto")
    @Mapping(target = "severity", source = "severityId")
    AllergyConditionDto toAllergyConditionDto(AllergyConditionBo allergyConditionBo);
}
