package ar.lamansys.odontology.infrastructure.repository.tooth;

import ar.lamansys.odontology.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OdontogramQuadrantStorageMockImpl implements OdontogramQuadrantStorage {


    @Override
    public List<OdontogramQuadrantBo> getAll() {
        List<OdontogramQuadrantBo> teethGroups = new ArrayList<>();
        for (int i=1; i<9 ; i++) {
            OdontogramQuadrantBo odontogramQuadrantBo = new OdontogramQuadrantBo();
            OdontologySnomedBo odontologySnomedBo = new OdontologySnomedBo();
            odontologySnomedBo.setPt("Cuadrante " + i);
            odontologySnomedBo.setSctid("235234234");
            odontogramQuadrantBo.setSnomed(odontologySnomedBo);
            teethGroups.add(odontogramQuadrantBo);
            odontogramQuadrantBo.setQuadrantCode(i);
        }
        return teethGroups;
    }
}
