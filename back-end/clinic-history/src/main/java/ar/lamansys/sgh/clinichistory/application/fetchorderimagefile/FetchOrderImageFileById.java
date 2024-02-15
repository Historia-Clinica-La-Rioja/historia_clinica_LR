package ar.lamansys.sgh.clinichistory.application.fetchorderimagefile;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileException;
import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileExceptionEnum;
import ar.lamansys.sgh.clinichistory.application.ports.OrderImageFileStorage;
import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileBo;
import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class FetchOrderImageFileById {

	private final OrderImageFileStorage orderImageFileStorage;

	private final FileService fileService;

	public StoredFileBo run(Integer id) throws FetchDocumentFileException {
		log.debug("FetchDocumentFile with id {}",id);
		return orderImageFileStorage.findById(id)
				.map(this::loadFile)
				.orElseThrow(() -> new FetchDocumentFileException(FetchDocumentFileExceptionEnum.UNKNOWN_DOCUMENT_FILE,
						String.format("No existe un archivo para el documento con id %s", id)));
	}

	private StoredFileBo loadFile(OrderImageFileBo orderImageFileBo) {
		var path = fileService.buildCompletePath(orderImageFileBo.getPath());
		var pdfFile = fileService.loadFile(path);
		return new StoredFileBo(
				pdfFile,
				MediaType.APPLICATION_PDF.toString(),
				orderImageFileBo.getName()
		);
	}
}
