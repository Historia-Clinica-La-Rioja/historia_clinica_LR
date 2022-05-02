package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
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
public class DiagnosticStorageImpl implements DiagnosticStorage {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticStorageImpl.class);

    private final EntityManager entityManager;

    public DiagnosticStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<DiagnosticBo> getDiagnostics() {
        LOG.debug("No input parameters");

        String sqlString =
                "SELECT ad.sctid, ad.pt, ad.applicable_to_tooth, ad.applicable_to_surface, " +
                "       ad.permanent_c, ad.permanent_p, ad.permanent_o, ad.temporary_c, ad.temporary_e, ad.temporary_o " +
                "FROM {h-schema}applicable_diagnostic ad " +
                "ORDER BY ad.pt ASC";

        Query query = entityManager.createNativeQuery(sqlString);
        List<Object[]> queryResult = query.getResultList();

        List<DiagnosticBo> result = queryResult
                .stream()
                .map(this::parseToDiagnosticBo)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<DiagnosticBo> getDiagnostic(String sctid) {
        LOG.debug("Input parameter -> sctid {}", sctid);

        String sqlString =
                "SELECT ad.sctid, ad.pt, ad.applicable_to_tooth, ad.applicable_to_surface, " +
                "       ad.permanent_c, ad.permanent_p, ad.permanent_o, ad.temporary_c, ad.temporary_e, ad.temporary_o " +
                "FROM {h-schema}applicable_diagnostic ad " +
                "WHERE ad.sctid = :sctid ";

        Query query = entityManager.createNativeQuery(sqlString)
                .setParameter("sctid", sctid);
        List<Object[]> diagnostics = query.getResultList();

        if (diagnostics.isEmpty())
            return Optional.empty();
        Optional<DiagnosticBo> result = diagnostics
                .stream()
                .findFirst()
                .map(this::parseToDiagnosticBo);
        LOG.trace("Output -> {}", result);
        return result;
    }

    private DiagnosticBo parseToDiagnosticBo(Object[] rawDiagnostic) {
        return new DiagnosticBo((String) rawDiagnostic[0], (String) rawDiagnostic[1],
                (boolean) rawDiagnostic[2], (boolean) rawDiagnostic[3],
                (boolean) rawDiagnostic[4], (boolean) rawDiagnostic[5],(boolean) rawDiagnostic[6],
                (boolean) rawDiagnostic[7], (boolean) rawDiagnostic[8],(boolean) rawDiagnostic[9]);
    }

}
