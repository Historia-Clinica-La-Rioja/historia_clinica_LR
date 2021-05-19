package net.pladema.clinichistory.documents.core.generalstate;

import net.pladema.clinichistory.documents.repository.generalstate.domain.AllergyConditionVo;
import net.pladema.clinichistory.documents.service.ips.domain.AllergyConditionBo;
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
