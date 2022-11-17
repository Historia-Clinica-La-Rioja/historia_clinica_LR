package ar.lamansys.refcounterref.application.createreferencefile;

import ar.lamansys.refcounterref.application.createreferencefile.exceptions.CreateReferenceFileException;
import ar.lamansys.refcounterref.application.createreferencefile.exceptions.CreateReferenceFileExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateReferenceFile {

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;


    public Integer run(Integer institutionId, Integer patientId, MultipartFile file) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);

        if (file == null)
            throw new CreateReferenceFileException(CreateReferenceFileExceptionEnum.NULL_FILE, "El archivo es obligatorio");
        if (institutionId == null)
            throw new CreateReferenceFileException(CreateReferenceFileExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (patientId == null)
            throw new CreateReferenceFileException(CreateReferenceFileExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");

        return referenceCounterReferenceFileStorage.save(institutionId, patientId, file, EReferenceCounterReferenceType.REFERENCIA.getId().intValue());
    }

}
