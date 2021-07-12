package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class DiagnosticStorageImpl implements DiagnosticStorage {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticStorageImpl.class);

    private final DiagnosticRepository diagnosticRepository;

    public DiagnosticStorageImpl(DiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    @Override
    public List<DiagnosticBo> getDiagnostics() {
        LOG.debug("No input parameters");
        List<DiagnosticBo> result = diagnosticRepository.getAll()
                .stream()
                .map(this::parseToDiagnosticBo)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private DiagnosticBo parseToDiagnosticBo(Object[] rawDiagnostic) {
        return new DiagnosticBo((String) rawDiagnostic[0], (String) rawDiagnostic[1],
                                (boolean) rawDiagnostic[2], (boolean) rawDiagnostic[3]);
    }

}
