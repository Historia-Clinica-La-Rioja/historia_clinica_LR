package ar.lamansys.odontology.domain;

import java.util.List;
import java.util.Optional;

public interface ProcedureStorage {

    List<ProcedureBo> getProcedures();

    Optional<ProcedureBo> getProcedure(String sctid);

}
