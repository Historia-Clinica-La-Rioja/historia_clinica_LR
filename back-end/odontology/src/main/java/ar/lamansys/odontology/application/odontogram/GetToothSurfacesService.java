package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.ToothSurfacesBo;

public interface GetToothSurfacesService {
    ToothSurfacesBo run(String toothId);
}
