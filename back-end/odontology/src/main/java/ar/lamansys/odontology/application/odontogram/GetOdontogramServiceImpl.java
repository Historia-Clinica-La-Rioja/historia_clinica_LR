package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.domain.OdontogramQuadrantStorage;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetOdontogramServiceImpl implements GetOdontogramService {

    private final ToothStorage toothStorage;
    private final OdontogramQuadrantStorage odontogramQuadrantStorage;
    private final Logger LOG;

    public GetOdontogramServiceImpl(
            ToothStorage toothStorage,
            OdontogramQuadrantStorage odontogramQuadrantStorage) {
        this.toothStorage = toothStorage;
        this.odontogramQuadrantStorage = odontogramQuadrantStorage;
        this.LOG = LoggerFactory.getLogger(getClass());
    }

    public List<OdontogramQuadrantBo> run() {
        var teeth = toothStorage.getAll();
        var quadrants = odontogramQuadrantStorage.getAll();
        HashMap<Integer, OdontogramQuadrantBo> quadrantMap = new HashMap<>();
        quadrants.forEach(q -> quadrantMap.put(q.getCode(), q));
        teeth.forEach(t -> quadrantMap.get(t.getQuadrantCode()).addTooth(t));
        var result = new ArrayList<>(quadrantMap.values());
        LOG.debug("Output -> {}", result);
        return result;
    }
}
