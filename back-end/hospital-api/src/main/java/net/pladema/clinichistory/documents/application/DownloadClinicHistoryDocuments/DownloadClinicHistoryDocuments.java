package net.pladema.clinichistory.documents.application.DownloadClinicHistoryDocuments;

import 	ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;

import ar.lamansys.sgx.shared.security.UserInfo;

import com.lowagie.text.DocumentException;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;

import net.pladema.clinichistory.documents.domain.CHDocumentBo;
import net.pladema.clinichistory.documents.domain.ECHEncounterType;
import net.pladema.clinichistory.documents.infrastructure.output.repository.ClinicHistoryContextBuilder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
		log.debug("Input parameters -> ids", ids);
		List<CHDocumentBo> documents = clinicHistoryStorage.getClinicHistoryDocuments(ids);
		if (documents.isEmpty())
			return null;
		Integer patientId = documents.stream().findFirst().get().getPatientId();
		List<InputStream> inputStreams = new ArrayList<>();
		LinkedHashMap<Pair<Integer, ECHEncounterType>, List<CHDocumentBo>> documentsByEpisode = mapDocumentsByEpisode(documents);
		documentsByEpisode.forEach((k,v) -> {
			if (k.getSecond().equals(ECHEncounterType.OUTPATIENT)){
				Map<String, Object> context = clinicHistoryContextBuilder.buildOutpatientContext(v.get(0), institutionId);
				inputStreams.add(pdfService.generate("clinic_history_outpatient", context).stream);
			}
			if(k.getSecond().equals(ECHEncounterType.HOSPITALIZATION)){
				Map<String, Object> context = clinicHistoryContextBuilder.buildEpisodeContext(k.getFirst(), v, institutionId, k.getSecond());
				inputStreams.add(pdfService.generate("clinic_history_episode", context).stream);
			}
			if (k.getSecond().equals(ECHEncounterType.EMERGENCY_CARE)){
				Map<String, Object> context = clinicHistoryContextBuilder.buildEpisodeContext(k.getFirst(), v, institutionId, k.getSecond());
				inputStreams.add(pdfService.generate("clinic_history_episode", context).stream);
			}
		});
		List<InputStream> enumeratedDocuments = enumeratePages(inputStreams);
		if(!inputStreams.isEmpty()){
			clinicHistoryStorage.savePatientClinicHistoryLastPrint(UserInfo.getCurrentAuditor(), patientId, institutionId);
			return new StoredFileBo(FileContentBo.fromBytes(pdfService.mergePdfFiles(enumeratedDocuments)), MediaType.APPLICATION_PDF.toString(), "HCE_" + patientId + ".pdf");
		}
		return null;
	}

	private LinkedHashMap<Pair<Integer, ECHEncounterType>, List<CHDocumentBo>> mapDocumentsByEpisode (List<CHDocumentBo> documents){
		List<Pair<Integer, ECHEncounterType>> sources = new ArrayList<>();
		LinkedHashMap<Pair<Integer, ECHEncounterType>, List<CHDocumentBo>> episodes = new LinkedHashMap<>();
		documents.forEach(doc -> {
			if (doc.getRequestSourceId() != null && (!sources.contains(Pair.of(doc.getRequestSourceId(), doc.getEncounterType())))) sources.add(Pair.of(doc.getRequestSourceId(), doc.getEncounterType()));
			if (doc.getRequestSourceId() == null && (!sources.contains(Pair.of(doc.getSourceId(), doc.getEncounterType())))) sources.add(Pair.of(doc.getSourceId(), doc.getEncounterType()));
		});
		sources.forEach(source -> {
			episodes.put(source, documents
					.stream()
					.filter(doc -> ((doc.getRequestSourceId() != null && doc.getRequestSourceId().equals(source.getFirst()) && doc.getEncounterType().equals(source.getSecond())) || (doc.getRequestSourceId() == null && doc.getSourceId().equals(source.getFirst()) && doc.getEncounterType().equals(source.getSecond()))))
					.collect(Collectors.toList()));
		});
		return episodes;
	}

	private List<InputStream> enumeratePages (List<InputStream> documents) throws IOException {
		List<InputStream> enumeratedDocuments = new ArrayList<>();

		int totalPages = 0;
		//Obtengo el número total de páginas
		for (InputStream doc: documents) {
			try (PDDocument document = PDDocument.load(doc)) {
				totalPages += document.getNumberOfPages();
				doc.reset();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		int totalCurrentPage = 1;
		for(InputStream doc: documents){
			try (PDDocument modifiedDocument = PDDocument.load(doc)) {
				int totalEncounterPages = modifiedDocument.getNumberOfPages();
				//PDDocumentCatalog catalog = modifiedDocument.getDocumentCatalog();

				for (int pageIterator = 0; pageIterator < totalEncounterPages; pageIterator++) {
					PDPage currentDocumentPage = modifiedDocument.getPage(pageIterator);

					float width = currentDocumentPage.getArtBox().getWidth() - 63f;
					float height = currentDocumentPage.getArtBox().getHeight() - 825f;

					try (PDPageContentStream contentStream = new PDPageContentStream(modifiedDocument, currentDocumentPage, PDPageContentStream.AppendMode.APPEND, true)) {
							// Agrega el número de página y el número total de páginas
							contentStream.beginText();
							contentStream.newLineAtOffset(width, height); // Ajusta la posición según tus necesidades
							contentStream.setFont(PDType1Font.HELVETICA, 7);
							contentStream.showText("Página " + totalCurrentPage + " de " + totalPages);
							contentStream.endText();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					totalCurrentPage ++;
				}
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				modifiedDocument.save(outputStream);
				modifiedDocument.close();
				enumeratedDocuments.add(new ByteArrayInputStream(outputStream.toByteArray()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return enumeratedDocuments;
	}

}
