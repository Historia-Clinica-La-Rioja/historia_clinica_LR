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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
		log.debug("Input parameters -> ids", ids);
		List<CHDocumentBo> documents = clinicHistoryStorage.getClinicHistoryDocuments(ids);
		Integer patientId = documents.stream().findFirst().get().getPatientId();
		List<CHDocumentBo> outpatientDocuments = documents.stream().filter(doc -> doc.getEncounterType().equals(ECHEncounterType.OUTPATIENT)).collect(Collectors.toList());
		Map<Integer, List<CHDocumentBo>> hospitalizationDocuments = mapDocumentsByEpisode(documents.stream().filter(doc -> doc.getEncounterType().equals(ECHEncounterType.HOSPITALIZATION)).collect(Collectors.toList()));
		Map<Integer, List<CHDocumentBo>> emergencyCareDocuments = mapDocumentsByEpisode(documents.stream().filter(doc -> doc.getEncounterType().equals(ECHEncounterType.EMERGENCY_CARE)).collect(Collectors.toList()));
		List<InputStream> inputStreams = new ArrayList<>();
		int totalPages = outpatientDocuments.size() + hospitalizationDocuments.size() + emergencyCareDocuments.size();
		AtomicInteger actualPage = new AtomicInteger();
		if(!outpatientDocuments.isEmpty()) {
			for (CHDocumentBo document : outpatientDocuments) {
				actualPage.getAndIncrement();
				Map<String, Object> context = clinicHistoryContextBuilder.buildOutpatientContext(document, institutionId);
				context.put("totalPages", totalPages);
				context.put("actualPage", actualPage);
				inputStreams.add(pdfService.generate("clinic_history_outpatient", context).stream);
			}
		}
		if(!hospitalizationDocuments.isEmpty()){
			hospitalizationDocuments.forEach((k,v) -> {
				actualPage.getAndIncrement();
				Map<String, Object> context = clinicHistoryContextBuilder.buildEpisodeContext(k, v, institutionId, ECHEncounterType.HOSPITALIZATION);
				context.put("totalPages", totalPages);
				context.put("actualPage", actualPage);
				inputStreams.add(pdfService.generate("clinic_history_episode", context).stream);
			});
		}
		if(!emergencyCareDocuments.isEmpty()){
			emergencyCareDocuments.forEach((k,v) -> {
				actualPage.getAndIncrement();
				Map<String, Object> context = clinicHistoryContextBuilder.buildEpisodeContext(k, v, institutionId, ECHEncounterType.EMERGENCY_CARE);
				context.put("totalPages", totalPages);
				context.put("actualPage", actualPage);
				inputStreams.add(pdfService.generate("clinic_history_episode", context).stream);
			});
		}
		if(!inputStreams.isEmpty()){
			clinicHistoryStorage.savePatientClinicHistoryLastPrint(UserInfo.getCurrentAuditor(), patientId, institutionId);
			return new StoredFileBo(FileContentBo.fromBytes(pdfService.mergePdfFiles(inputStreams)), MediaType.APPLICATION_PDF.toString(), "HCE_" + patientId + ".pdf");
		}
		return null;
	}

	private Map<Integer, List<CHDocumentBo>> mapDocumentsByEpisode (List<CHDocumentBo> documents){
		List<Integer> sourceIds = new ArrayList<>();
		Map<Integer, List<CHDocumentBo>> episodes = new HashMap<>();
		documents.forEach(doc -> {
			if (doc.getRequestSourceId() != null && !sourceIds.contains(doc.getRequestSourceId())) sourceIds.add(doc.getRequestSourceId());
			if (doc.getRequestSourceId() == null && !sourceIds.contains(doc.getSourceId())) sourceIds.add(doc.getSourceId());
		});
		sourceIds.forEach(sourceId -> {
			episodes.put(sourceId, documents.stream().filter(doc -> ((doc.getRequestSourceId() != null && doc.getRequestSourceId().equals(sourceId)) || (doc.getRequestSourceId() == null && doc.getSourceId().equals(sourceId)))).sorted(Comparator.comparing(CHDocumentBo::getCreatedOn)).collect(Collectors.toList()));
		});
		return episodes;
	}

}
