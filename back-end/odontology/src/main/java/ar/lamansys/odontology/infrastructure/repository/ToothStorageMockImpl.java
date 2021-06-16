package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToothStorageMockImpl implements ToothStorage {

    private final Logger logger;

    public ToothStorageMockImpl() {
        logger = LoggerFactory.getLogger(getClass());
    }

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
            toothBo.setToothCode((short) toothCode);
            toothBo.setQuadrantCode((short) quadrantCode);

            OdontologySnomedBo odontologySnomedBo = new OdontologySnomedBo();
            odontologySnomedBo.setPt("Diente " + i);
            odontologySnomedBo.setSctid("232323" +  + quadrantCode + toothCode);
            toothBo.setSnomed(odontologySnomedBo);
            teeth.add(toothBo);
        }
        logger.trace("Output -> {}", teeth);
        return teeth;
    }

    @Override
    public Optional<ToothBo> get(String toothId) {
        logger.trace("Input -> {}", toothId);
        var search = getAll()
                .stream()
                .filter(t -> t.getSnomed().getSctid().equals(toothId))
                .collect(Collectors.toList());
        ToothBo toothBo = null;
        if (search.size() == 1 ) {
            toothBo = search.get(0);
            toothBo.setPosterior(toothBo.getToothCode() > 3);
        }
        Optional<ToothBo> result =  Optional.ofNullable(toothBo);
        logger.trace("Output -> {}", result);
        return result;
    }
}
