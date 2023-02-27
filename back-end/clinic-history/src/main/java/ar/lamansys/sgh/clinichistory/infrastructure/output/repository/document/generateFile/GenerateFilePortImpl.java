package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ar.lamansys.sgh.clinichistory.domain.document.event.GenerateFilePort;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
@Component
public class GenerateFilePortImpl implements GenerateFilePort {

	private final FileService fileService;
	private final PdfService pdfService;
	private final AuditableContextBuilder auditableContextBuilder;

	@Override
	public Optional<DocumentFile> save(OnGenerateDocumentEvent event) {
		Map<String,Object> contextMap = auditableContextBuilder.buildContext(event.getDocumentBo(), event.getPatientId());

		formatStringDates(contextMap);

		var path = fileService.buildCompletePath(event.getRelativeDirectory());
		String realFileName = event.getUuid();
		String fictitiousFileName = event.buildDownloadName();
		try {
			var pdfStream = pdfService.generate(event.getTemplateName(), contextMap);
			var file = fileService.saveStreamInPath(path, realFileName, "DOCUMENTO_DE_ENCUENTRO",false, pdfStream);
			return Optional.of(new DocumentFile(
					event.getDocumentBo().getId(),
					event.getEncounterId(),
					event.getSourceType(),
					event.getDocumentTypeId(), path.relativePath, fictitiousFileName, file.getUuidfile(), file.getChecksum()));
		} catch (PDFDocumentException e) {
			log.error("Save document file -> {}", event, e);
			throw e;
		}
	}

	private void formatStringDates(Map<String, Object> context){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		ArrayList<ImmunizationInfoDto> vaccinesData = (ArrayList<ImmunizationInfoDto>) context.get("nonBillableImmunizations");
		vaccinesData.stream().filter(immunizationInfoDto -> immunizationInfoDto.getAdministrationDate() != null)
				.forEach(immunizationInfoDto -> immunizationInfoDto.setAdministrationDate(LocalDate.parse(immunizationInfoDto.getAdministrationDate()).format(dateTimeFormatter)));
	}

}
