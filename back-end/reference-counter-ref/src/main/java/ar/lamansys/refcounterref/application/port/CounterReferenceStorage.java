package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;

import java.util.List;
import java.util.Optional;

public interface CounterReferenceStorage {

    Integer save(CounterReferenceInfoBo counterReferenceInfoBo);

    Optional<CounterReferenceSummaryBo> getCounterReference(Integer referenceId);

    List<CounterReferenceProcedureBo> getProceduresByCounterReference(Integer counterReferenceId);

}