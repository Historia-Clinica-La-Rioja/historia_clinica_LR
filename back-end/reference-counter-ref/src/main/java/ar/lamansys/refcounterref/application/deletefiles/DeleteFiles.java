package ar.lamansys.refcounterref.application.deletefiles;

import ar.lamansys.refcounterref.application.deletefiles.exceptions.DeleteFilesException;
import ar.lamansys.refcounterref.application.deletefiles.exceptions.DeleteFilesExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteFiles {

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;


    public void run(List<Integer> fileIdList) {
        log.debug("Input parameters -> fileIdList {}", fileIdList);

        if (fileIdList.isEmpty())
            throw new DeleteFilesException(DeleteFilesExceptionEnum.EMPTY_FILE_IDS, "Es obligatorio al menos un id de archivo");

        referenceCounterReferenceFileStorage.deleteFiles(fileIdList);
    }

}
