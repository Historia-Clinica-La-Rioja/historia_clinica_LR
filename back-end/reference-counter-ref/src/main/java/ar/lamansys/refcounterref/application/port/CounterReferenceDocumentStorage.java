package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.document.CounterReferenceDocumentBo;

public interface CounterReferenceDocumentStorage {

    void save(CounterReferenceDocumentBo counterReferenceDocumentBo);

}