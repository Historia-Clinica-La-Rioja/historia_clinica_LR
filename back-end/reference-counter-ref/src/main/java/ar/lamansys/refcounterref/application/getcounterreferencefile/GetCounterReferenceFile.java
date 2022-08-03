package ar.lamansys.refcounterref.application.getcounterreferencefile;

import ar.lamansys.refcounterref.application.getcounterreferencefile.exceptions.GetCounterReferenceFileException;
import ar.lamansys.refcounterref.application.getcounterreferencefile.exceptions.GetCounterReferenceFileExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.file.StoredFileBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCounterReferenceFile {

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

    public StoredFileBo run(Integer fileId) {
        log.debug("Input parameters -> fileId {}", fileId);

        if (fileId == null)
            throw new GetCounterReferenceFileException(GetCounterReferenceFileExceptionEnum.NULL_FILE_ID, "El id del archivo es obligatorio");

        StoredFileBo result = referenceCounterReferenceFileStorage.getFile(fileId, EReferenceCounterReferenceType.CONTRARREFERENCIA.getId().intValue());
        return result;
    }

}
