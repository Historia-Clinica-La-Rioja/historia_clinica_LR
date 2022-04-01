package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProcedureStorage;
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
public class ProcedureStorageImpl implements ProcedureStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ProcedureStorageImpl.class);

    private final EntityManager entityManager;

    public ProcedureStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ProcedureBo> getProcedures() {
        LOG.debug("No input parameters");

        String sqlString =
                "SELECT ap.sctid, ap.pt, ap.applicable_to_tooth, ap.applicable_to_surface, " +
                "       ap.permanent_c, ap.permanent_p, ap.permanent_o, ap.temporary_c, ap.temporary_e, ap.temporary_o " +
                "FROM applicable_procedure ap " +
                "ORDER BY ap.pt ASC";

        Query query = entityManager.createNativeQuery(sqlString);
        List<Object[]> queryResult = query.getResultList();

        List<ProcedureBo> result = queryResult
                .stream()
                .map(this::parseToProcedureBo)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<ProcedureBo> getProcedure(String sctid) {
        LOG.debug("Input parameter -> sctid {}", sctid);

        String sqlString =
                "SELECT ap.sctid, ap.pt, ap.applicable_to_tooth, ap.applicable_to_surface, " +
                "       ap.permanent_c, ap.permanent_p, ap.permanent_o, ap.temporary_c, ap.temporary_e, ap.temporary_o " +
                "FROM {h-schema}applicable_procedure ap " +
                "WHERE ap.sctid = :sctid ";

        Query query = entityManager.createNativeQuery(sqlString)
                .setParameter("sctid", sctid);
        List<Object[]> procedures = query.getResultList();

        if (procedures.isEmpty())
            return Optional.empty();
        Optional<ProcedureBo> result = procedures
                .stream()
                .findFirst()
                .map(this::parseToProcedureBo);
        LOG.trace("Output -> {}", result);
        return result;
    }

    private ProcedureBo parseToProcedureBo(Object[] rawProcedure) {
        return new ProcedureBo((String) rawProcedure[0], (String) rawProcedure[1],
                (boolean) rawProcedure[2], (boolean) rawProcedure[3],
                (boolean) rawProcedure[4], (boolean) rawProcedure[5],(boolean) rawProcedure[6],
                (boolean) rawProcedure[7], (boolean) rawProcedure[8],(boolean) rawProcedure[9]);
    }
}
