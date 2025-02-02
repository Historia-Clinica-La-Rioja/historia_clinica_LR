package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ProcedureDescriptionDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface ProcedureDescriptionMapper {

    @Named("toProcedureDescriptionDto")
    @Mapping(target = "anesthesiaStartLocalDate", source = "anesthesiaStartDate")
    @Mapping(target = "anesthesiaStartLocalTime", source = "anesthesiaStartTime")
    @Mapping(target = "anesthesiaEndLocalDate", source = "anesthesiaEndDate")
    @Mapping(target = "anesthesiaEndLocalTime", source = "anesthesiaEndTime")
    @Mapping(target = "surgeryStartLocalDate", source = "surgeryStartDate")
    @Mapping(target = "surgeryStartLocalTime", source = "surgeryStartTime")
    @Mapping(target = "surgeryEndLocalDate", source = "surgeryEndDate")
    @Mapping(target = "surgeryEndLocalTime", source = "surgeryEndTime")
    @Mapping(target = "foodIntake", source = "foodIntake")
    @Mapping(target = "foodIntakeDate", source = "foodIntakeDate")
    @Mapping(target = "foodIntakeClockTime", source = "foodIntake")
    @Mapping(target = "foodIntakeClockDate", source = "foodIntakeDate")
    ProcedureDescriptionDto toProcedureDescriptionDto(ProcedureDescriptionBo procedureDescriptionBo);

    @Named("toProcedureDescriptionBo")
    @Mapping(target = "foodIntake", source = "foodIntake")
    @Mapping(target = "foodIntakeDate", source = "foodIntakeDate")
    ProcedureDescriptionBo toProcedureDescriptionBo(ProcedureDescriptionDto procedureDescriptionDto);

}
