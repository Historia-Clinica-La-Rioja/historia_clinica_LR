package net.pladema.clinichistory.documents.core.generalstate;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.AllergyConditionVo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface AllergyConditionServiceMapper {

    @Named("toListAllergyConditionBo")
    @IterableMapping(qualifiedByName = "toAllergyConditionBo")
    List<AllergyConditionBo> toListAllergyConditionBo(List<AllergyConditionVo> allergyConditionVoList);
}
