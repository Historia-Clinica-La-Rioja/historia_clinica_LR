package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.ToothBo;

public interface GetToothService {
    ToothBo run(String toothId);
}
