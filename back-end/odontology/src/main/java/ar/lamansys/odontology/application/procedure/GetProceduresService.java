package ar.lamansys.odontology.application.procedure;

import ar.lamansys.odontology.domain.ProcedureBo;

import java.util.List;

public interface GetProceduresService {

    List<ProcedureBo> run();

}
