package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;

public interface CounterReferenceStorage {

    Integer save(CounterReferenceInfoBo counterReferenceInfoBo);

}