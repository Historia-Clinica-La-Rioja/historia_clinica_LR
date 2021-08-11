package ar.lamansys.odontology.infrastructure.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ToothRepositoryImpl implements ToothRepository {

    private final EntityManager entityManager;
    private final Logger logger;

    public ToothRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Transactional(readOnly = true)
    public List<Object[]> getAll() {

        String sqlString = "SELECT bp.sctid, bp.pt, t.code, t.quadrant_code " +
                "FROM tooth t " +
                "JOIN body_part bp ON bp.sctid = t.sctid";

        Query query = entityManager.createNativeQuery(sqlString);
        List<Object[]> result = query.getResultList();
        logger.debug("Output -> {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public Object[] get(String toothId) {
        logger.debug("Input -> {}", toothId);
        String sqlString = "SELECT bp.sctid, bp.pt, t.code, t.quadrant_code " +
                "FROM tooth t " +
                "JOIN body_part bp ON bp.sctid = t.sctid " +
                "WHERE t.sctid = :toothId ";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("toothId", toothId);
        List<Object[]> result = query.getResultList();
        logger.debug("Output -> {}", result);
        return result.size() == 1 ? result.get(0) : null;
    }
}
