package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class ToothStorageImpl implements ToothStorage {

    private final Logger logger;
    private final ToothRepository toothRepository;

    public ToothStorageImpl(ToothRepository toothRepository) {
        logger = LoggerFactory.getLogger(getClass());
        this.toothRepository = toothRepository;
    }


    @Override
    public List<ToothBo> getAll() {
        List<Object[]> resultSearch = toothRepository.getAll();
        List<ToothBo> result = resultSearch.stream().map(this::parseToToothBo).collect(Collectors.toList());
        logger.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<ToothBo> get(String toothId) {
        logger.debug("Input -> {}", toothId);
        Object[] resultSearch = toothRepository.get(toothId);
        Optional<ToothBo> result = Optional.ofNullable(resultSearch).map(this::parseToToothBo);
        logger.debug("Output -> {}", result);
        return result;
    }

    public ToothBo parseToToothBo(Object[] toothRaw) {
        logger.debug("Input -> {}", toothRaw);

        OdontologySnomedBo odontologySnomedBo = new OdontologySnomedBo();
        odontologySnomedBo.setSctid((String) toothRaw[0]);
        odontologySnomedBo.setPt((String) toothRaw[1]);

        ToothBo result = new ToothBo();
        result.setSnomed(odontologySnomedBo);
        result.setToothCode((Short) toothRaw[2]);
        result.setQuadrantCode((Short) toothRaw[3]);

        logger.debug("Output -> {}", result);
        return result;
    }
}
