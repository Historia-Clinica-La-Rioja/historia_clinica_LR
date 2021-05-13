package ar.lamansys.odontology.application.fetchodontogram;

import ar.lamansys.odontology.domain.OdontogramQuadrantStorage;
import ar.lamansys.odontology.domain.TeethGroupBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetOdontogramInfoServiceImpl implements GetOdontogramInfoService {

    private final ToothStorage toothStorage;
    private final OdontogramQuadrantStorage odontogramQuadrantStorage;
    private final Logger LOG;

    public GetOdontogramInfoServiceImpl(
            ToothStorage toothStorage,
            OdontogramQuadrantStorage odontogramQuadrantStorage) {
        this.toothStorage = toothStorage;
        this.odontogramQuadrantStorage = odontogramQuadrantStorage;
        this.LOG = LoggerFactory.getLogger(getClass());
    }


    //

    @Override
    public List<TeethGroupBo> run() {
        var teeth = toothStorage.getAll();
        var quadrants = odontogramQuadrantStorage.getAll();
        List<TeethGroupBo> result = quadrants.stream().map(q ->
                new TeethGroupBo(
                        q,
                        teeth.stream().filter(t -> (t.getQuadrant().equals(q.getQuadrantCode())))
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());
        LOG.debug("Response validate -> {}", result);
        return result;
    }
}
