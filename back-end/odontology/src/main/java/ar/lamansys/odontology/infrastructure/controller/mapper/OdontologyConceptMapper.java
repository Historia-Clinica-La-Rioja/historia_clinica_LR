package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontologyConceptDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface OdontologyConceptMapper {

    @Named("fromDiagnosticBo")
    OdontologyConceptDto fromDiagnosticBo(DiagnosticBo diagnosticBo);

    @Named("fromDiagnosticBoList")
    @IterableMapping(qualifiedByName = "fromDiagnosticBo")
    List<OdontologyConceptDto> fromDiagnosticBoList(List<DiagnosticBo> diagnosticBoList);

    @Named("fromProcedureBo")
    OdontologyConceptDto fromProcedureBo(ProcedureBo procedureBo);

    @Named("fromProcedureBoList")
    @IterableMapping(qualifiedByName = "fromProcedureBo")
    List<OdontologyConceptDto> fromProcedureBoList(List<ProcedureBo> procedureBoList);

}
