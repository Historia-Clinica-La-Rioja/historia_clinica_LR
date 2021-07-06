package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProceduresStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProceduresStorageMockImpl implements ProceduresStorage {

    private final Logger LOG;

    public ProceduresStorageMockImpl() {
        LOG = LoggerFactory.getLogger(getClass());
    }

    @Override
    public List<ProcedureBo> getProcedures() {
        LOG.debug("No input parameters");
        List<ProcedureBo> result = new ArrayList<>();
        result.add(new ProcedureBo("183120001", "desgaste de prótesis", false, false));
        result.add(new ProcedureBo("31890001", "alargamiento quirurgico de corona clinica", true, true));
        result.add(new ProcedureBo("398911000221105", "apicectomia", true, false));
        result.add(new ProcedureBo("398921000221100", "aplicación de cariostáticos en piezas dentarias", true, true));
        result.add(new ProcedureBo("234710007", "medicación intermedia intraconducto", true, false));
        result.add(new ProcedureBo("313042009", "barniz fluorado", true, false));
        result.add(new ProcedureBo("35889000", "topicación de fluor", true, false));
        result.add(new ProcedureBo("80764003", "incrustacion estética", true, true));
        result.add(new ProcedureBo("57673005", "incrustación metalica", false, true));
        result.add(new ProcedureBo("468785006", "barra metalica sobre implantes", true, false));
        result.add(new ProcedureBo("48635004", "biopsia por aspiracion", false, false));
        result.add(new ProcedureBo("129249002", "biopsia por puncion", false, false));
        LOG.trace("Output -> {}", result);
        return result;
    }
}
