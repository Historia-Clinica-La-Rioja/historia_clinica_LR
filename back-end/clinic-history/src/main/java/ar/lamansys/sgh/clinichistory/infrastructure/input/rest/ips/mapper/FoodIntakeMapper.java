package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.FoodIntakeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.FoodIntakeDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = LocalDateMapper.class)
public interface FoodIntakeMapper {

    @Named("toFoodIntakeDto")
    FoodIntakeDto toFoodIntakeDto(FoodIntakeBo foodIntakeBo);

    @Named("toFoodIntakeBo")
    FoodIntakeBo toFoodIntakeBo(FoodIntakeDto foodIntakeDto);
}
