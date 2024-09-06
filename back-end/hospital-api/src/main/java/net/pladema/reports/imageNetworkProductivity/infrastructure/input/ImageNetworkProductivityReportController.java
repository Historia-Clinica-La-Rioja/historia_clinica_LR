package net.pladema.reports.imageNetworkProductivity.infrastructure.input;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.reports.imageNetworkProductivity.application.GenerateImageNetworkProductivitySheet;

import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

@Slf4j
@Tag(name = "ImageNetworkProductivityReportController", description = "Controller used to generate image network productivity reports")
@AllArgsConstructor
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA')")
@RestController
@RequestMapping("/institution/{institutionId}/report/image-network-productivity")
public class ImageNetworkProductivityReportController {

	private GenerateImageNetworkProductivitySheet generateImageNetworkProductivitySheet;

	private LocalDateMapper localDateMapper;

	@GetMapping
	public ResponseEntity<Resource> run(
		@PathVariable("institutionId") Integer institutionId,
		@RequestParam(value="fromDate") String fromDate,
		@RequestParam(value="toDate") String toDate,
		@RequestParam(value="clinicalSpecialtyId", required = false) Short clinicalSpecialtyId,
		@RequestParam(value="doctorId", required = false) Integer doctorId,
		@RequestParam(value="hierarchicalUnitTypeId", required = false) Integer hierarchicalUnitTypeId,
		@RequestParam(value="hierarchicalUnitId", required = false) Integer hierarchicalUnitId,
		@RequestParam(value="appointmentStateId", required = false) Short appointmentStateId,
		@RequestParam(value="includeHierarchicalUnitDescendants", required = false) boolean includeHierarchicalUnitDescendants
	) {
		log.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, hierarchicalUnitTypeId {}, hierarchicalUnitId {}, appointmentStateId {}, includeHierarchicalUnitDescendants {}" ,
				institutionId, fromDate, toDate, hierarchicalUnitTypeId, hierarchicalUnitId, appointmentStateId, includeHierarchicalUnitDescendants);
		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);
		ImageNetworkProductivityFilterBo filter = new ImageNetworkProductivityFilterBo(institutionId, startDate, endDate, clinicalSpecialtyId, doctorId);
		IWorkbook result = generateImageNetworkProductivitySheet.run(filter);
		return StoredFileResponse.sendFile(buildReport(result), generateFileName(result.getExtension()), result.getContentType());
	}

	private String generateFileName(String extension) {
		return "reporte_productividad_rdi." + extension;
	}

	private FileContentBo buildReport(IWorkbook workbook) {
		return StreamsUtils.writeToContent((out) -> {
			try {
				workbook.write(out);
			} catch (Exception e) {
				throw streamException(e);
			}
		});
	}

}
