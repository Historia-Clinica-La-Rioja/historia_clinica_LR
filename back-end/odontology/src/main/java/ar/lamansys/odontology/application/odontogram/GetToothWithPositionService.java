package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.ToothWithPositionBo;

public interface GetToothWithPositionService {
    ToothWithPositionBo run(String toothId);
}
