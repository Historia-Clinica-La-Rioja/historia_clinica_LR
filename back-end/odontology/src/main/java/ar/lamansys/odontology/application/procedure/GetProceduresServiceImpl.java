package ar.lamansys.odontology.application.procedure;

import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProceduresStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetProceduresServiceImpl implements GetProceduresService {

    private static final Logger LOG = LoggerFactory.getLogger(GetProceduresServiceImpl.class);

    private final ProceduresStorage proceduresStorage;

    public GetProceduresServiceImpl(ProceduresStorage proceduresStorage) {
        this.proceduresStorage = proceduresStorage;
    }

    @Override
    public List<ProcedureBo> run() {
        LOG.debug("No input parameters");
        List<ProcedureBo> result = proceduresStorage.getProcedures();
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
