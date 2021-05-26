package ar.lamansys.odontology.infrastructure.repository.tooth;

import ar.lamansys.odontology.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontogramQuadrantStorageMockImpl implements OdontogramQuadrantStorage {


    @Override
    public List<OdontogramQuadrantBo> getAll() {
        return List.of(
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1230, "Cuadrante " + 1),
                        1,
                        false,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1231, "Cuadrante " + 2),
                        2,
                        true,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1232, "Cuadrante 3" + 3),
                        3,
                        true,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1233, "Cuadrante " + 4),
                        4,
                        false,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1234, "Cuadrante " + 5),
                        5,
                        false,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1235, "Cuadrante " + 6),
                        6,
                        true,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1236, "Cuadrante " + 7),
                        7,
                        true,
                        false,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo(1237, "Cuadrante " + 8),
                        8,
                        false,
                        false,
                        false
                )
        );

    }
}
