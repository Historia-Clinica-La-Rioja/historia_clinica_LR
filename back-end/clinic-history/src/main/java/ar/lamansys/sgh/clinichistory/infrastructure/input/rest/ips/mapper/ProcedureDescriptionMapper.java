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
    @Mapping(target = "anesthesiaStartLocalDate", source = "anesthesiaStartDate")
    @Mapping(target = "anesthesiaStartLocalTime", source = "anesthesiaStartTime")
    @Mapping(target = "anesthesiaEndLocalDate", source = "anesthesiaEndDate")
    @Mapping(target = "anesthesiaEndLocalTime", source = "anesthesiaEndTime")
    @Mapping(target = "surgeryStartLocalDate", source = "surgeryStartDate")
    @Mapping(target = "surgeryStartLocalTime", source = "surgeryStartTime")
    @Mapping(target = "surgeryEndLocalDate", source = "surgeryEndDate")
    @Mapping(target = "surgeryEndLocalTime", source = "surgeryEndTime")
    ProcedureDescriptionDto toProcedureDescriptionDto(ProcedureDescriptionBo procedureDescriptionBo);

    @Named("toProcedureDescriptionBo")
    ProcedureDescriptionBo toProcedureDescriptionBo(ProcedureDescriptionDto procedureDescriptionDto);

}
