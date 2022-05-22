package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class ToothStorageImpl implements ToothStorage {

    private final Logger logger;

    private final EntityManager entityManager;

    public ToothStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        logger = LoggerFactory.getLogger(getClass());
    }


    @Override
    public List<ToothBo> getAll() {
        logger.debug("No input parameters");

        String sqlString = "SELECT bp.sctid, bp.pt, t.code, t.quadrant_code, t.posterior " +
                "FROM {h-schema}tooth t " +
                "JOIN {h-schema}body_part bp ON bp.sctid = t.sctid";
        Query query = entityManager.createNativeQuery(sqlString);

        List<Object[]> resultSearch = query.getResultList();
        List<ToothBo> result = resultSearch.stream().map(this::parseToToothBo).collect(Collectors.toList());
        logger.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<ToothBo> get(String toothId) {
        logger.debug("Input -> {}", toothId);

        String sqlString = "SELECT bp.sctid, bp.pt, t.code, t.quadrant_code, t.posterior " +
                "FROM {h-schema}tooth t " +
                "JOIN {h-schema}body_part bp ON bp.sctid = t.sctid " +
                "WHERE t.sctid = :toothId ";
        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("toothId", toothId);

        List<Object[]> queryResult = query.getResultList();
        Object[] resultSearch = queryResult.size() == 1 ? queryResult.get(0) : null;
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
        result.setPosterior((boolean) toothRaw[4]);

        logger.debug("Output -> {}", result);
        return result;
    }
}
