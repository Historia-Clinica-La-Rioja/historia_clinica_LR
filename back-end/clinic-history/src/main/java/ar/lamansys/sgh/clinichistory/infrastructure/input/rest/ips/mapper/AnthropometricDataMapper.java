package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnthropometricDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface AnthropometricDataMapper {

    @Named("fromAnthropometricDataDto")
    AnthropometricDataBo fromAnthropometricDataDto(AnthropometricDataDto anthropometricDataDto);

    @Named("fromAnthropometricDataBo")
    AnthropometricDataDto fromAnthropometricDataBo(AnthropometricDataBo anthropometricDataBo);

}
