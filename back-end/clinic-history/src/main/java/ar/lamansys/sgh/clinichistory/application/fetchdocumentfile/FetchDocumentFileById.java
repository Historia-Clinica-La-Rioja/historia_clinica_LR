package ar.lamansys.sgh.clinichistory.application.fetchdocumentfile;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileException;
import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileExceptionEnum;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FetchDocumentFileById {

    private final DocumentFileStorage documentFileStorage;


    public FetchDocumentFileById(DocumentFileStorage documentFileStorage) {
        this.documentFileStorage = documentFileStorage;
    }

    public DocumentFileBo run(Long id) throws FetchDocumentFileException {
        log.debug("FetchDocumentFile with id {}",id);
        return documentFileStorage.findById(id)
                .orElseThrow(() -> new FetchDocumentFileException(FetchDocumentFileExceptionEnum.UNKNOWN_DOCUMENT_FILE,
                        String.format("No existe un archivo para el documento con id %s", id)));
    }
}
