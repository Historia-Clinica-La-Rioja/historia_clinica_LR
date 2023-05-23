package net.pladema.terminology.cache.infrastructure.output.repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileService;
import lombok.AllArgsConstructor;
import net.pladema.snowstorm.services.loadCsv.UpdateConceptsResultBo;
import net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsByCsv;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileIngestor;

@Service
@AllArgsConstructor
public class SnomedCacheFileIngestorImpl implements SnomedCacheFileIngestor {
	private final UpdateSnomedConceptsByCsv updateSnomedConceptsByCsv;
	private final FileService fileService;

	@Override
	public UpdateConceptsResultBo run(Long fileId, String eclKey) {
		var fileContent = fileService.loadFile(fileId);
		return updateSnomedConceptsByCsv.updateSnomedConcepts(new InputStreamSource() {
			private ByteArrayOutputStream byteArrayOutputStreamCache = null;

			private ByteArrayOutputStream byteArrayOutputStream() throws IOException {
				if (byteArrayOutputStreamCache == null) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					fileContent.stream.transferTo(baos);
					byteArrayOutputStreamCache = baos;
				}
				return byteArrayOutputStreamCache;
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(byteArrayOutputStream().toByteArray());
			}
		}, eclKey);
	}

}
