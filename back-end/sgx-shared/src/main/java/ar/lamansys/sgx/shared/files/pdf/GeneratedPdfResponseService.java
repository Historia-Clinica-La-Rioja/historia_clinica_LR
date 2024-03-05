package ar.lamansys.sgx.shared.files.pdf;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.GeneratedBlobBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GeneratedPdfResponseService {

	private final PdfService pdfService;

	public GeneratedBlobBo generatePdf(EPDFTemplate template, Map<String,Object> contextMap, String filename) {
		return new GeneratedBlobBo(
				pdfService.generate(template.name, contextMap),
				MediaType.APPLICATION_PDF,
				filename + ".pdf"
		);
	}

}
