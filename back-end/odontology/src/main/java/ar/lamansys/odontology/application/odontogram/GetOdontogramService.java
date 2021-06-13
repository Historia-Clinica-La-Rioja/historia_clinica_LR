package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import java.util.List;

public interface GetOdontogramService {
    List<OdontogramQuadrantBo> run();
}
