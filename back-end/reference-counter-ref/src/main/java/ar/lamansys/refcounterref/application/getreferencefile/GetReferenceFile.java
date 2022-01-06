package ar.lamansys.refcounterref.application.getreferencefile;

import ar.lamansys.refcounterref.application.createreferencefile.exceptions.CreateReferenceFileException;
import ar.lamansys.refcounterref.application.createreferencefile.exceptions.CreateReferenceFileExceptionEnum;
import ar.lamansys.refcounterref.application.getreferencefile.exceptions.GetReferenceFileException;
import ar.lamansys.refcounterref.application.getreferencefile.exceptions.GetReferenceFileExceptionEnum;
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
public class GetReferenceFile {

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

    @Transactional
    public StoredFileBo run(Integer fileId) {
        log.debug("Input parameters -> fileId {}", fileId);

        if (fileId == null)
            throw new GetReferenceFileException(GetReferenceFileExceptionEnum.NULL_FILE_ID, "El id del archivo es obligatorio");

        StoredFileBo result = referenceCounterReferenceFileStorage.getFile(fileId, EReferenceCounterReferenceType.REFERENCIA.getId().intValue());
        return result;
    }

}
