package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        result.add(new DiagnosticBo("64969001", "ausencia parcial congénita de los dientes (trastorno)", true, false)); // N/A
        result.add(new DiagnosticBo("699685006", "surco oclusal profundo (hallazgo)", false, true));
        result.add(new DiagnosticBo("109564008", "caries dental asociada con hipomineralización del esmalte (trastorno)", false, true));
        result.add(new DiagnosticBo("-1000", "Mancha Blanca", false, true));
        result.add(new DiagnosticBo("-2000", "restos radiculares", true, false));
        result.add(new DiagnosticBo("9984005", "exfoliación de dientes por enfermedad sistémica (trastorno)", true, false));
        result.add(new DiagnosticBo("112481000119103", "restauración dental insatisfactoria (hallazgo)", false, true));
        // procedures that act as diagnostics
        result.add(new DiagnosticBo("80764003", "incrustacion estética", true, true));
        result.add(new DiagnosticBo("468785006", "barra metalica sobre implantes", true, false));// puente
        result.add(new DiagnosticBo("398981000221101", "corona de acero provisoria por destrucción coronaria.", true, false));
        result.add(new DiagnosticBo("789147006", "implante dental", true, false));
        result.add(new DiagnosticBo("278123008", "implante osteointegrado con forma radicular", true, true));
        result.add(new DiagnosticBo("4721000221105", "inactivación de caries", false, true));
        result.add(new DiagnosticBo("399041000221101", "obturación con amalgama cavidad simple", false, true));
        result.add(new DiagnosticBo("398791000221100", "puente por tecnologia cad cam", true, false));// puente
        result.add(new DiagnosticBo("234713009", "selladores de surcos, fosas y fisuras", false, true));
        result.add(new DiagnosticBo("398761000221105", "tramo de puente", true, false));// puente
        result.add(new DiagnosticBo("399001000221103", "endodoncia multirradicular", true, false));
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<DiagnosticBo> getDiagnostic(String sctid) {
        LOG.debug("Input parameter -> sctid {}", sctid);
        DiagnosticBo diagnostic = new DiagnosticBo(sctid, "ausencia congénita completa de dientes (trastorno)", true, false);
        LOG.debug("Output -> {}", diagnostic);
        return Optional.of(diagnostic);
    }
}
