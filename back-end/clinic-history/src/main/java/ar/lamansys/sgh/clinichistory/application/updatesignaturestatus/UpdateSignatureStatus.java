package ar.lamansys.sgh.clinichistory.application.updatesignaturestatus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.DigitalSignatureStorage;
import ar.lamansys.sgh.clinichistory.domain.document.digitalsignature.DigitalSignatureCallbackBo;
import ar.lamansys.sgh.shared.infrastructure.output.entities.ESignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgx.shared.files.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateSignatureStatus {

	private final DigitalSignatureStorage digitalSignatureStorage;
	private final FileService fileService;

	public void run(DigitalSignatureCallbackBo digitalSignatureBo) {
		log.debug("Input parameter -> digitalSignatureBo {}", digitalSignatureBo);
		Boolean success = digitalSignatureBo.getStatus().getSuccess();
		DocumentFile file = getDocumentFile(digitalSignatureBo.getDocumentId(), success);
		if (success)
			saveDocument(file.getFilepath(), digitalSignatureBo);
		else
			log.error(digitalSignatureBo.getStatus().getMsg());
		digitalSignatureStorage.updateFile(file);
	}


	private void insertDigitalSignature(FilePathBo path, DigitalSignatureCallbackBo signatureCallbackData) throws IOException {
		byte[] signatureHash = Base64.getDecoder().decode(signatureCallbackData.getSignatureHash());
		PDDocument document = PDDocument.load(fileService.loadFile(path).getStream());

		PDSignature signature = new PDSignature();
		signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
		signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
		signature.setSignDate(DateUtils.getCurrentCalendarWithNoTime());

		document.addSignature(signature);
		document.setDocumentId(signatureCallbackData.getDocumentId());
		try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
			ExternalSigningSupport externalSigningSupport = document.saveIncrementalForExternalSigning(fos);
			externalSigningSupport.setSignature(signatureHash);
			document.close();
		}
    }

	private void saveDocument(String filepath, DigitalSignatureCallbackBo signature) {
		var path = fileService.buildCompletePath(filepath);
		try {
			insertDigitalSignature(path, signature);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private DocumentFile getDocumentFile(Long id, Boolean success) {
		DocumentFile file = digitalSignatureStorage.getFile(id);
		Short statusId = success ? ESignatureStatus.SIGNED.getId() : ESignatureStatus.PENDING.getId();
		file.setSignatureStatusId(statusId);
		return file;
	}

}
