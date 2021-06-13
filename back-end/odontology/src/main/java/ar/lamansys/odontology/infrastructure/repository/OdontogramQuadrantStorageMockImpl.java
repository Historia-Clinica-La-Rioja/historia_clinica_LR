package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontogramQuadrantStorageMockImpl implements OdontogramQuadrantStorage {


    @Override
    public List<OdontogramQuadrantBo> getAll() {
        return List.of(
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114121", "Cuadrante " + 1),
                        1,
                        false,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114122", "Cuadrante " + 2),
                        2,
                        true,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114123", "Cuadrante 3" + 3),
                        3,
                        true,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114124", "Cuadrante " + 4),
                        4,
                        false,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114125", "Cuadrante " + 5),
                        5,
                        false,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114126", "Cuadrante " + 6),
                        6,
                        true,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114127", "Cuadrante " + 7),
                        7,
                        true,
                        false,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114128", "Cuadrante " + 8),
                        8,
                        false,
                        false,
                        false
                )
        );

    }
}
