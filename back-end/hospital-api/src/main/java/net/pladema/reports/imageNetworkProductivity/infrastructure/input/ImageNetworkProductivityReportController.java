package net.pladema.reports.imageNetworkProductivity.infrastructure.input;

import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.reports.imageNetworkProductivity.application.GenerateImageNetworkProductivitySheet;

import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import net.pladema.reports.imageNetworkProductivity.infrastructure.input.dto.ImageNetworkProductivityFilterDto;

import net.pladema.reports.imageNetworkProductivity.infrastructure.input.mapper.ImageNetworkProductivityReportMapper;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

@Slf4j
@Tag(name = "ImageNetworkProductivityReportController", description = "Controller used to generate image network productivity reports")
@AllArgsConstructor
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA')")
@RestController
@RequestMapping("/institution/{institutionId}/report/image-network-productivity")
public class ImageNetworkProductivityReportController {

	private ImageNetworkProductivityReportMapper imageNetworkProductivityReportMapper;

	private GenerateImageNetworkProductivitySheet generateImageNetworkProductivitySheet;

	private ObjectMapper objectMapper;

	@GetMapping
	public ResponseEntity<Resource> run(@PathVariable("institutionId") Integer institutionId,
										@RequestParam("filter") String imageNetworkProductivityFilterDto) throws JsonProcessingException {
		log.debug("Input parameters -> institutionId {}, imageNetworkProductivityFilterDto {}", institutionId, imageNetworkProductivityFilterDto);
		ImageNetworkProductivityFilterBo filter = parseFilter(imageNetworkProductivityFilterDto, institutionId);
		IWorkbook result = generateImageNetworkProductivitySheet.run(filter);
		return StoredFileResponse.sendFile(buildReport(result), generateFileName(result.getExtension()), result.getContentType());
	}

	private String generateFileName(String extension) {
		return "reporte_productividad_rdi." + extension;
	}

	private ImageNetworkProductivityFilterBo parseFilter(String filter, Integer institutionId) throws JsonProcessingException {
		ImageNetworkProductivityFilterDto imageNetworkProductivityFilterDto = objectMapper.readValue(filter, ImageNetworkProductivityFilterDto.class);
		ImageNetworkProductivityFilterBo result = imageNetworkProductivityReportMapper.fromImageNetworkProductivityFilterDto(imageNetworkProductivityFilterDto);
		result.setInstitutionId(institutionId);
		return result;
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
