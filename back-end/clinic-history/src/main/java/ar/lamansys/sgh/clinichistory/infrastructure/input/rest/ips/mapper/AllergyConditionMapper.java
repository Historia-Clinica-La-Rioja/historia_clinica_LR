package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AllergyConditionDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface AllergyConditionMapper {

    @Named("toAllergyConditionDto")
    AllergyConditionDto toAllergyConditionDto(AllergyConditionBo allergyConditionBo);
}
