package ar.lamansys.odontology.infrastructure.repository.tooth;

import ar.lamansys.odontology.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OdontogramQuadrantStorageMockImpl implements OdontogramQuadrantStorage {


    @Override
    public List<OdontogramQuadrantBo> getAll() {
        return List.of(
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 1),
                        1,
                        false,
                        true,
                        true,
                        null
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 2),
                        2,
                        true,
                        true,
                        true,
                        null
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 3),
                        3,
                        true,
                        false,
                        true,
                        null
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 4),
                        4,
                        false,
                        false,
                        true,
                        null
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 5),
                        5,
                        false,
                        true,
                        false,
                        null
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 6),
                        6,
                        true,
                        true,
                        false,
                        null
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 7),
                        7,
                        true,
                        false,
                        false,
                        null
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("235234234", "Cuadrante " + 8),
                        8,
                        false,
                        false,
                        false,
                        null
                )
        );

    }
}
