package ar.lamansys.odontology.application.fetchodontogram;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import java.util.List;

public interface GetOdontogramInfoService {
    List<OdontogramQuadrantBo> run();
}
