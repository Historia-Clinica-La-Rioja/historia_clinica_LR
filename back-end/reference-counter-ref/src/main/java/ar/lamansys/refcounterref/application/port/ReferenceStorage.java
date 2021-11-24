package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.ReferenceBo;

import java.util.List;

public interface ReferenceStorage {
    void save(List<ReferenceBo> referenceBoList);
}
