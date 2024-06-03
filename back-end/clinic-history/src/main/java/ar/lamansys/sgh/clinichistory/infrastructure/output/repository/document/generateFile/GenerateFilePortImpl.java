package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.lamansys.sgh.clinichistory.domain.document.event.GenerateFilePort;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateFilePortImpl implements GenerateFilePort {

	private final FileService fileService;
	private final PdfService pdfService;
	@Qualifier(value = "generate_document_context_visitor")
	private final DocumentVisitor generateDocumentContext;

	@Override
	public Optional<DocumentFile> save(OnGenerateDocumentEvent event) {

		IDocumentBo documentBo = event.getDocumentBo();
		documentBo.accept(generateDocumentContext);
		Map<String,Object> contextMap = documentBo.getContextMap();

		formatStringDates(contextMap);

		var path = fileService.buildCompletePath(event.getRelativeDirectory());
		String realFileName = event.getUuid();
		String fictitiousFileName;
		if (event.getDocumentType().equals(EDocumentType.DIGITAL_RECIPE.getValue()))
			fictitiousFileName = generateDigitalRecipeFileName(contextMap);
		else
			fictitiousFileName = event.buildDownloadName();

		try {
			var pdfStream = !event.getTemplateName().equals(EDocumentType.DIGITAL_RECIPE.getTemplate())
			? pdfService.generate(event.getTemplateName(), contextMap)
			: pdfService.customizableGenerate(event.getTemplateName(), contextMap);
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
		if(vaccinesData != null) {
			vaccinesData.stream()
					.filter(immunizationInfoDto -> immunizationInfoDto.getAdministrationDate() != null)
					.forEach(immunizationInfoDto -> immunizationInfoDto.setAdministrationDate(LocalDate.parse(immunizationInfoDto.getAdministrationDate())
							.format(dateTimeFormatter)));
		}
	}


	private String generateDigitalRecipeFileName(Map<String,Object> context) {
		String recipeNumber = (String) context.get("recipeNumber");
		String identificationNumber = ((BasicPatientDto) context.get("patient")).getIdentificationNumber();
		return identificationNumber + "_" + recipeNumber + ".pdf";
	}

}
