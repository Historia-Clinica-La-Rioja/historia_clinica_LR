package net.pladema.clinichistory.requests.servicerequests.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import net.pladema.clinichistory.requests.servicerequests.repository.OrderImageFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.OrderImageFile;
import net.pladema.clinichistory.requests.servicerequests.service.UploadTranscribedOrderFileService;

@Service
public class UploadTranscribedOrderFileServiceImpl implements UploadTranscribedOrderFileService {

	private final FileService fileService;

	private final OrderImageFileRepository orderImageFileRepository;

	private static final String RELATIVE_DIRECTORY = "/patient/{patiendId}/trasncribed-orders/{orderId}/";
	private static final Logger LOG = LoggerFactory.getLogger(UploadTranscribedOrderFileServiceImpl.class);
	private final String OUTPUT = "Output -> {}";

	public UploadTranscribedOrderFileServiceImpl(FileService fileService, OrderImageFileRepository orderImageFileRepository) {
		this.fileService = fileService;
		this.orderImageFileRepository = orderImageFileRepository;
	}

	@Override
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
		LOG.debug(OUTPUT, result);
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
		LOG.debug(OUTPUT, result);
		return result;
	}

	private String buildPartialPath(Integer patientId, String relativeFilePath, Integer orderId){
		LOG.debug("Input parameters -> patientId {}, relativeFilePath {}", patientId, relativeFilePath);
		String result = RELATIVE_DIRECTORY
				.replace("{patiendId}", patientId.toString())
				.replace("{orderId}", orderId.toString())
				.concat(relativeFilePath);
		LOG.debug(OUTPUT, result);
		return result;
	}


}
