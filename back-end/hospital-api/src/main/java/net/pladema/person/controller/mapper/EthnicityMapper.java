package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.EthnicityDto;
import net.pladema.person.service.domain.EthnicityBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface EthnicityMapper {

    @Named("fromEthnicityBo")
    EthnicityDto fromEthnicityBo(EthnicityBo ethnicityBo);

    @Named("fromEthnicityBoList")
    @IterableMapping(qualifiedByName = "fromEthnicityBo")
    List<EthnicityDto> fromEthnicityBoList(List<EthnicityBo> ethnicityBoList);

}
