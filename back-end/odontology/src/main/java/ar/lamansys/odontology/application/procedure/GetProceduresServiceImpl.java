package ar.lamansys.odontology.application.procedure;

import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProcedureStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetProceduresServiceImpl implements GetProceduresService {

    private static final Logger LOG = LoggerFactory.getLogger(GetProceduresServiceImpl.class);

    private final ProcedureStorage procedureStorage;

    public GetProceduresServiceImpl(ProcedureStorage procedureStorage) {
        this.procedureStorage = procedureStorage;
    }

    @Override
    public List<ProcedureBo> run() {
        LOG.debug("No input parameters");
        List<ProcedureBo> result = procedureStorage.getProcedures();
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
