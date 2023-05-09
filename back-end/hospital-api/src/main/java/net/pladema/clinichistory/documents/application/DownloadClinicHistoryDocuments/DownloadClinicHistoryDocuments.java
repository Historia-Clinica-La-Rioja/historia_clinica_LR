package net.pladema.clinichistory.documents.application.DownloadClinicHistoryDocuments;

import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;

import ar.lamansys.sgx.shared.security.UserInfo;

import com.lowagie.text.DocumentException;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;

import net.pladema.clinichistory.documents.domain.CHDocumentBo;
import net.pladema.clinichistory.documents.domain.ECHEncounterType;
import net.pladema.clinichistory.documents.infrastructure.output.repository.ClinicHistoryContextBuilder;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DownloadClinicHistoryDocuments {

	private final ClinicHistoryStorage clinicHistoryStorage;
	private final ClinicHistoryContextBuilder clinicHistoryContextBuilder;
	private final PdfService pdfService;

	public DownloadClinicHistoryDocuments(ClinicHistoryStorage clinicHistoryStorage,
										  ClinicHistoryContextBuilder clinicHistoryContextBuilder,
										  PdfService pdfService)
	{
		this.clinicHistoryStorage = clinicHistoryStorage;
		this.clinicHistoryContextBuilder = clinicHistoryContextBuilder;
		this.pdfService = pdfService;
	}

	public StoredFileBo run (List<Long> ids, Integer institutionId) throws DocumentException, IOException {
		log.debug("Input parameters -> ids {}", ids);
		List<CHDocumentBo> documents = clinicHistoryStorage.getClinicHistoryDocuments(ids)
				.stream()
				.filter(doc -> doc.getEncounterType().equals(ECHEncounterType.OUTPATIENT))
				.collect(Collectors.toList());
		if(!documents.isEmpty()){
			Integer patientId = documents.stream().findFirst().get().getPatientId();
			List<InputStream> inputStreams = new ArrayList<>();
			int totalPages = documents.size();
			int actualPage = 0;
			for (CHDocumentBo document: documents){
				actualPage ++;
				Map<String, Object> context = clinicHistoryContextBuilder.buildContext(document, institutionId);
				context.put("totalPages", totalPages);
				context.put("actualPage", actualPage);
				inputStreams.add(pdfService.generate("clinic_history_outpatient", context).stream);
			}
			clinicHistoryStorage.savePatientClinicHistoryLastPrint(UserInfo.getCurrentAuditor(), patientId, institutionId);
			return new StoredFileBo(FileContentBo.fromBytes(pdfService.mergePdfFiles(inputStreams)), MediaType.APPLICATION_PDF.toString(), "HCE_" + patientId + ".pdf");
		}
		return null;
	}

}
