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
        int quadrant;
        int position;
        String code;
        int t = 0;
        int w = 0;
        for (int i=0; i<52 ; i++) {
            quadrant = w + ( (i - t) / quadrantSize + 1 );
            if (quadrant==5) {
                quadrantSize = 5;
                t = 32;
                w = 4;
            }
            position = (i - t) % quadrantSize + 1;
            code = quadrant + Integer.toString(position);

            ToothBo toothBo = new ToothBo();
            toothBo.setCode(code);
            OdontologySnomedBo odontologySnomedBo = new OdontologySnomedBo();
            odontologySnomedBo.setPt("Diente " + i);
            odontologySnomedBo.setSctid("12124125124");
            toothBo.setSnomed(odontologySnomedBo);
            teeth.add(toothBo);
        }
        return teeth;
    }
}
