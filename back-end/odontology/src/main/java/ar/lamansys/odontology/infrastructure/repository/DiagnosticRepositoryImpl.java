package ar.lamansys.odontology.infrastructure.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class DiagnosticRepositoryImpl implements DiagnosticRepository{

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticRepositoryImpl.class);

    private final EntityManager entityManager;

    public DiagnosticRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Object[]> getAll() {

        String sqlString = "SELECT s.sctid, s.pt, ad.applicable_to_tooth, ad.applicable_to_surface " +
                "FROM applicable_diagnostic ad " +
                "JOIN snomed s ON s.sctid = ad.sctid";

        Query query = entityManager.createNativeQuery(sqlString);
        List<Object[]> result = query.getResultList();
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
