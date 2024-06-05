package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.FoodIntakeDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ProcedureDescriptionDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface ProcedureDescriptionMapper {

    @Named("toFoodIntakeDto")
    @Mapping(target = "clockTime", source = "foodIntake")
    @Mapping(target = "performedTime", source = "foodIntake")
    FoodIntakeDto toFoodIntakeDto(ProcedureDescriptionBo procedureDescriptionBo);

    @Named("toProcedureDescriptionDto")
    ProcedureDescriptionDto toProcedureDescriptionDto(ProcedureDescriptionBo procedureDescriptionBo);

    @Named("toProcedureDescriptionBo")
    ProcedureDescriptionBo toProcedureDescriptionBo(ProcedureDescriptionDto procedureDescriptionDto);

}
