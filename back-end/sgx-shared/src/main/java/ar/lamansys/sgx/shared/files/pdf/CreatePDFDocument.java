package ar.lamansys.sgx.shared.files.pdf;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class CreatePDFDocument {

	private final PdfService pdfService;

	public <T> StoredFileBo run(String template, String filename, T data, GenerateDocumentContext<T> strategy) {
		log.debug("Input parameters -> template {}, filename {}, data {}, strategy {}", template, filename, data, strategy);
		Map<String, Object> context = strategy.run(data);
		FileContentBo content = pdfService.generate(template, context);
		StoredFileBo result = new StoredFileBo(content, MediaType.APPLICATION_PDF.toString(), getCompleteFileName(filename));
		log.debug("Output -> {}", result);
		return result;
	}

	private String getCompleteFileName(String filename) {
		final String PDF_EXTENSION = ".pdf";
		return filename.concat(PDF_EXTENSION);
	}

}
