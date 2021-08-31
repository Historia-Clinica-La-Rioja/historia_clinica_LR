package ar.lamansys.odontology.infrastructure.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ProcedureRepositoryImpl implements ProcedureRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ProcedureRepositoryImpl.class);

    private final EntityManager entityManager;

    public ProcedureRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Object[]> getAll() {

        String sqlString = "SELECT s.sctid, s.pt, ap.applicable_to_tooth, ap.applicable_to_surface " +
                "FROM applicable_procedure ap " +
                "JOIN snomed s ON s.sctid = ap.sctid " +
                "ORDER BY s.pt ASC";

        Query query = entityManager.createNativeQuery(sqlString);
        List<Object[]> result = query.getResultList();
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public List<Object[]> getBySctid(String sctid) {

        String sqlString = "SELECT s.sctid, s.pt, ap.applicable_to_tooth, ap.applicable_to_surface " +
                "FROM applicable_procedure ap " +
                "JOIN snomed s ON s.sctid = ap.sctid " +
                "WHERE ap.sctid = :sctid ";

        Query query = entityManager.createNativeQuery(sqlString)
                .setParameter("sctid", sctid);
        List<Object[]> result = query.getResultList();
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
