package ar.lamansys.refcounterref.application.deletereference;

import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteReference {

    private final ReferenceStorage referenceStorage;

    public void run(Integer referenceId) {
        log.debug("Input parameters -> referenceId {} ", referenceId);
        referenceStorage.delete(referenceId);
        log.debug("Delete success --> referenceId {} ", referenceId);
    }
}
