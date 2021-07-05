package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiagnosticStorageMockImpl implements DiagnosticStorage {

    private final Logger LOG;

    public DiagnosticStorageMockImpl() {
        LOG = LoggerFactory.getLogger(getClass());
    }

    @Override
    public List<DiagnosticBo> getDiagnostics() {
        LOG.debug("No input parameters");
        List<DiagnosticBo> result = new ArrayList<>();
        result.add(new DiagnosticBo("16958000", "ausencia congénita completa de dientes (trastorno)", true, false));
        result.add(new DiagnosticBo("64969001", "ausencia parcial congénita de los dientes (trastorno)", true, false));
        result.add(new DiagnosticBo("367534004", "diente supernumerario (trastorno)", true, false));
        result.add(new DiagnosticBo("29553002", "dientes con forma de clavija (trastorno)", true, false));
        result.add(new DiagnosticBo("109473003", "amelogénesis imperfecta, tipo hipoplásica con microdoncia (trastorno)", true, true));
        result.add(new DiagnosticBo("266415009", "anomalía de desarrollo del tamaño y la forma de diente (trastorno)", true, false));
        result.add(new DiagnosticBo("30265004", "surco oclusal profundo (hallazgo)", false, true));
        result.add(new DiagnosticBo("49414002", "opacidades del esmalte no causadas por flúor (trastorno)", true, true));
        result.add(new DiagnosticBo("80764003", "incrustacion estética", true, true));
        result.add(new DiagnosticBo("57673005", "incrustación metalica", false, true));
        result.add(new DiagnosticBo("398891000221108", "carilla dental", false, true));
        result.add(new DiagnosticBo("398981000221101", "corona de acero provisoria por destrucción coronaria.", true, false));
        LOG.trace("Output -> {}", result);
        return result;
    }
}
