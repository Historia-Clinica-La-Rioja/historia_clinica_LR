package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.EthnicityDto;
import net.pladema.person.service.domain.EthnicityBo;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
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

    @Named("fromEthnicityDto")
    EthnicityBo fromEthnicityDto(EthnicityDto ethnicityDto);

    @Named("fromEthnicityDtoList")
    @IterableMapping(qualifiedByName = "fromEthnicityDto")
    List<EthnicityBo> fromEthnicityDtoList(List<EthnicityDto> ethnicityDtoList);

    @Named("fromSnowstormItemResponse")
    default EthnicityDto fromSnowstormItemResponse(SnowstormItemResponse snowstormItemResponse) {
        EthnicityDto ethnicityDto = new EthnicityDto();
        ethnicityDto.setSctid(snowstormItemResponse.getConceptId());
        ethnicityDto.setPt(snowstormItemResponse.getPt().findValue("term").asText());
        return ethnicityDto;
    }

    @Named("fromSnowstormItemResponseList")
    @IterableMapping(qualifiedByName = "fromSnowstormItemResponse")
    List<EthnicityDto> fromSnowstormItemResponseList(List<SnowstormItemResponse> snowstormItemResponseList);

}
