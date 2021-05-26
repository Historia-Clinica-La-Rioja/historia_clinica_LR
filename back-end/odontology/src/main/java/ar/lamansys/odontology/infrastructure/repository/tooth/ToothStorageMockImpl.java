package ar.lamansys.odontology.infrastructure.repository.tooth;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToothStorageMockImpl implements ToothStorage {


    @Override
    public List<ToothBo> getAll() {
        List<ToothBo> teeth = new ArrayList<>();
        int quadrantSize = 8;
        int quadrantCode;
        int toothCode;
        int t = 0;
        int w = 0;
        for (int i=0; i<52 ; i++) {
            quadrantCode = w + ( (i - t) / quadrantSize + 1 );
            if (quadrantCode == 5) {
                quadrantSize = 5;
                t = 32;
                w = 4;
            }
            toothCode = (i - t) % quadrantSize + 1;

            ToothBo toothBo = new ToothBo();
            toothBo.setToothCode(toothCode);
            toothBo.setQuadrantCode(quadrantCode);

            OdontologySnomedBo odontologySnomedBo = new OdontologySnomedBo();
            odontologySnomedBo.setPt("Diente " + i);
            odontologySnomedBo.setSctid("232323" +  + quadrantCode + toothCode);
            toothBo.setSnomed(odontologySnomedBo);
            teeth.add(toothBo);
        }
        return teeth;
    }
}
