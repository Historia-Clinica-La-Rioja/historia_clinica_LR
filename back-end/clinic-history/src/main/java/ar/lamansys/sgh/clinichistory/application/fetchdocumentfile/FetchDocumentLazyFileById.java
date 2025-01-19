package ar.lamansys.sgh.clinichistory.application.fetchdocumentfile;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileException;
import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileExceptionEnum;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;
import ar.lamansys.sgx.shared.filestorage.application.FetchFileResource;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.BlobLazyFileBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class FetchDocumentLazyFileById {

    private final DocumentFileStorage documentFileStorage;

	private final FetchFileResource fetchFileResource;

    public BlobLazyFileBo run(Long id) throws FetchDocumentFileException {
        log.debug("FetchDocumentFile with id {}",id);
        return documentFileStorage.findById(id)
				.map(this::loadFile)
                .orElseThrow(() -> new FetchDocumentFileException(FetchDocumentFileExceptionEnum.UNKNOWN_DOCUMENT_FILE,
                        String.format("No existe un archivo para el documento con id %s", id)));
    }

	private BlobLazyFileBo loadFile(DocumentFileBo documentFile) {
		return fetchFileResource.run(documentFile.getFilepath());
	}
}
