package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProcedureStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedureStorageMockImpl implements ProcedureStorage {

    private final Logger LOG;

    public ProcedureStorageMockImpl() {
        LOG = LoggerFactory.getLogger(getClass());
    }

    @Override
    public List<ProcedureBo> getProcedures() {
        LOG.debug("No input parameters");
        List<ProcedureBo> result = new ArrayList<>();
        result.add(new ProcedureBo("80764003", "incrustacion estética", true, true));
        result.add(new ProcedureBo("468785006", "barra metalica sobre implantes", true, false));
        result.add(new ProcedureBo("398981000221101", "corona de acero provisoria por destrucción coronaria.", true, false));
        result.add(new ProcedureBo("789147006", "implante dental", true, false));
        result.add(new ProcedureBo("278123008", "implante osteointegrado con forma radicular", true, true));
        result.add(new ProcedureBo("4721000221105", "inactivación de caries", false, true));
        result.add(new ProcedureBo("399041000221101", "obturación con amalgama cavidad simple", false, true));
        result.add(new ProcedureBo("398791000221100", "puente por tecnologia cad cam", true, false));
        result.add(new ProcedureBo("234713009", "selladores de surcos, fosas y fisuras", false, true));
        result.add(new ProcedureBo("398761000221105", "tramo de puente", true, false));
        result.add(new ProcedureBo("404198007", "extracción dentaria de retenidos", true, false));
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<ProcedureBo> getProcedure(String sctid) {
        LOG.debug("Input parameter -> sctid {}", sctid);
        ProcedureBo procedure = new ProcedureBo(sctid, "obturación con amalgama cavidad simple", false, true);
        LOG.debug("Output -> {}", procedure);
        return Optional.of(procedure);
    }
}
