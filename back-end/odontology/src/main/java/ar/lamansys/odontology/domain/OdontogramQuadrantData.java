package ar.lamansys.odontology.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OdontogramQuadrantData {

    private OdontogramQuadrantData() {}

    public static List<OdontogramQuadrantBo> getQuadrants() {
        return List.of(
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554970081", "Cuadrante superior derecho dentición permanente"),
                        1,
                        false,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554810031", "Cuadrante superior izquierdo dentición permanente"),
                        2,
                        true,
                        true,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554800021", "Cuadrante inferior izquierdo dentición permanente"),
                        3,
                        true,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554950001", "Cuadrante inferior derecho dentición permanente"),
                        4,
                        false,
                        false,
                        true
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554970082", "Cuadrante superior derecho dentición temporaria"),
                        5,
                        false,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554800022", "Cuadrante superior izquierdo dentición temporaria"),
                        6,
                        true,
                        true,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554810032", "Cuadrante inferior izquierdo dentición temporaria"),
                        7,
                        true,
                        false,
                        false
                ),
                new OdontogramQuadrantBo(
                        new OdontologySnomedBo("-2554950002", "Cuadrante inferior derecho dentición temporaria"),
                        8,
                        false,
                        false,
                        false
                )
        );
    }

    public static Map<Integer, OdontogramQuadrantBo> getAsMap() {
        HashMap<Integer, OdontogramQuadrantBo> quadrantMap = new HashMap<>();
        getQuadrants().forEach(q -> quadrantMap.put(q.getCode(), q));
        return quadrantMap;
    }
}
