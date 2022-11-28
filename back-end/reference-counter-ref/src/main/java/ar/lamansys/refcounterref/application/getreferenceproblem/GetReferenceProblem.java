package ar.lamansys.refcounterref.application.getreferenceproblem;

import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetReferenceProblem {

    private final ReferenceStorage referenceStorage;

    public List<ReferenceProblemBo> run(Integer patientId) {
        log.debug("Input parameters -> patientId {} ", patientId);
        return referenceStorage.getReferencesProblems(patientId);
    }
}
