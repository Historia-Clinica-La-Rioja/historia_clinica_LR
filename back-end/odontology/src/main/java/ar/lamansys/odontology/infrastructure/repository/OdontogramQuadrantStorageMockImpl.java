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
                        (short)1,
                        false,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114122", "Cuadrante " + 2),
                        (short)2,
                        true,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114123", "Cuadrante 3" + 3),
                        (short)3,
                        true,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114124", "Cuadrante " + 4),
                        (short)4,
                        false,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114125", "Cuadrante " + 5),
                        (short)5,
                        false,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114126", "Cuadrante " + 6),
                        (short)6,
                        true,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114127", "Cuadrante " + 7),
                        (short)7,
                        true,
                        false,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("123114128", "Cuadrante " + 8),
                        (short)8,
                        false,
                        false,
                        false
                )
        );

    }
}
