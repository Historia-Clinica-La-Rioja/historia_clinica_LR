package net.pladema.clinichistory.requests.transcribed.application.uploadfile;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.OrderImageFileRepository;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.entity.OrderImageFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UploadTranscribedOrderFile {

	private final FileService fileService;
	private final OrderImageFileRepository orderImageFileRepository;

	private static final String RELATIVE_DIRECTORY = "/patient/{patiendId}/trasncribed-orders/{orderId}/";
	private final String OUTPUT = "Output -> {}";
	@Transactional // Transaccion compleja
	public List<Integer> execute(MultipartFile[] files, Integer orderId, Integer patientId) {
		List<Integer> result = Arrays.stream(files).mapToInt(file -> {
					String newFileName = fileService.createFileName(FilenameUtils.getExtension(file.getOriginalFilename()));
					var path = fileService.buildCompletePath(
							buildPartialPath(patientId, newFileName, orderId)
					);
					String uuid = newFileName.split("\\.")[0];
					fileService.transferMultipartFile(path, uuid, "ORDER-IMAGE", file);
					return saveDiagnosticReportFileMetadata(path, file, orderId);
				})
				.boxed()
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	private Integer saveDiagnosticReportFileMetadata(FilePathBo path, MultipartFile file, Integer orderId) {
		OrderImageFile orderImageFile = new OrderImageFile(
				path.relativePath,
				file.getContentType(),
				file.getSize(),
				file.getOriginalFilename(),
				orderId);
		Integer result = orderImageFileRepository.save(orderImageFile).getId();
		log.debug(OUTPUT, result);
		return result;
	}

	private String buildPartialPath(Integer patientId, String relativeFilePath, Integer orderId){
		log.debug("Input parameters -> patientId {}, relativeFilePath {}", patientId, relativeFilePath);
		String result = RELATIVE_DIRECTORY
				.replace("{patiendId}", patientId.toString())
				.replace("{orderId}", orderId.toString())
				.concat(relativeFilePath);
		log.debug(OUTPUT, result);
		return result;
	}


}
