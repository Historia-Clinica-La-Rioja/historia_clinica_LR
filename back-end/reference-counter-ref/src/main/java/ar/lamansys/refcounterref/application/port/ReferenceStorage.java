package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceGetBo;

import java.util.List;

public interface ReferenceStorage {

    void save(List<ReferenceBo> referenceBoList);

    List<ReferenceGetBo> getReferences(Integer patientId, List<Integer> clinicalSpecialtyIds);

}
