package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ProcedureDescriptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ProcedureDescriptionMapper {

    @Named("toProcedureDescriptionDto")
    ProcedureDescriptionDto toProcedureDescriptionDto(ProcedureDescriptionBo procedureDescriptionBo);

    @Named("toProcedureDescriptionBo")
    ProcedureDescriptionBo toProcedureDescriptionBo(ProcedureDescriptionDto procedureDescriptionDto);
}
