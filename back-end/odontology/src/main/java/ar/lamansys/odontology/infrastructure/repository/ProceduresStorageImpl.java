package ar.lamansys.odontology.infrastructure.repository;

import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProceduresStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class ProceduresStorageImpl implements ProceduresStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ProceduresStorageImpl.class);

    private final ProcedureRepository procedureRepository;

    public ProceduresStorageImpl(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public List<ProcedureBo> getProcedures() {
        LOG.debug("No input parameters");
        List<ProcedureBo> result = procedureRepository.getAll()
                .stream()
                .map(this::parseToProcedureBo)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private ProcedureBo parseToProcedureBo(Object[] rawProcedure) {
        return new ProcedureBo((String) rawProcedure[0], (String) rawProcedure[1],
                               (boolean) rawProcedure[2], (boolean) rawProcedure[3]);
    }
}
