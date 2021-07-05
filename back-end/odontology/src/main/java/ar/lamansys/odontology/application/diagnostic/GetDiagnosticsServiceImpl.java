package ar.lamansys.odontology.application.diagnostic;

import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDiagnosticsServiceImpl implements GetDiagnosticsService {

    private static final Logger LOG = LoggerFactory.getLogger(GetDiagnosticsServiceImpl.class);

    private final DiagnosticStorage diagnosticStorage;

    public GetDiagnosticsServiceImpl(DiagnosticStorage diagnosticStorage) {
        this.diagnosticStorage = diagnosticStorage;
    }

    @Override
    public List<DiagnosticBo> run() {
        LOG.debug("No input parameters");
        List<DiagnosticBo> result = diagnosticStorage.getDiagnostics();
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
