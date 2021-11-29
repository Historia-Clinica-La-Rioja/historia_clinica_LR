package net.pladema.snvs.application.fetchmanualclassification;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.domain.event.SnvsEventManualClassificationsBo;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import net.pladema.snvs.infrastructure.output.repository.snvs.SnvsStorageImpl;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Conditional(SnvsCondition.class)
public class FetchManualClassification {

    public static final String OUTPUT = "Output -> {}";

    private final SnvsStorageImpl snvsStorage;

    public FetchManualClassification(SnvsStorageImpl snvsStorage) {
        this.snvsStorage = snvsStorage;
    }

    public List<SnvsEventManualClassificationsBo> run(SnvsProblemBo problemBo){
        log.debug("FetchManualClassification from problemBo {}", problemBo);
        return snvsStorage.fetchManualClassification(problemBo);
    }

}
