package ar.lamansys.sgh.clinichistory.application.signDocumentFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;
import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDocumentContentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;

import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.DigitalSignatureStorage;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.application.signDocumentFile.exceptions.SignDocumentFileEnumException;
import ar.lamansys.sgh.clinichistory.application.signDocumentFile.exceptions.SignDocumentFileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignDocumentFile {

	private final DocumentFileStorage documentFileStorage;

	private final DigitalSignatureStorage digitalSignatureStorage;

	private final SharedHospitalUserPort hospitalUserPort;

	private final FileService fileService;

	public String run(Integer institutionId, List<Long> documentIds, Integer userId) {
		log.debug("Input parameter -> institutionId {}, documentsIds {}, userId {}",institutionId, documentIds, userId);
		assertDocumentCreator(documentIds, userId);
		String professionalCuit = getProfesisonalCuit(userId);
		Integer personId = hospitalUserPort.getUserCompleteInfo(userId).getPersonId();
		List<DigitalSignatureDocumentContentDto> documents = getDocumentFiles(documentIds);
		String result = digitalSignatureStorage.generateDigitalSigningLink(new DigitalSignatureDataDto(professionalCuit, personId, documents, institutionId));
		log.debug("Output result URL -> {}", result);
		return result;
	}

	private String getProfesisonalCuit(Integer userId){
		return hospitalUserPort.getCuitByUserId(userId)
				.orElseThrow(()-> new SignDocumentFileException(SignDocumentFileEnumException.CUIL_NOT_EXISTS, "El profesional no posee cuil"));
	}

	private List<DigitalSignatureDocumentContentDto> getDocumentFiles(List<Long> documentIds) {
		return documentIds.stream().map(documentId -> {
			var file = digitalSignatureStorage.getFile(documentId);
			FilePathBo path = fileService.buildCompletePath(file.getFilepath());
			FilePathBo newPath = fileService.buildCompletePath(getNewRelativeDirectory(file.getFilepath(), UUID.randomUUID().toString()));
			try {
				String hash = generateEmptySignature(path, newPath, documentId);
				documentFileStorage.updateDigitalSignatureHash(documentId, hash);
				newPath.toFile().delete();
				return new DigitalSignatureDocumentContentDto(documentId, newPath, hash);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	private String generateEmptySignature(FilePathBo truePath, FilePathBo copyPath, Long documentId) throws IOException {
		PDDocument document = PDDocument.load(fileService.loadFile(truePath).getStream());
		PDSignature signature = new PDSignature();
		signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
		signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
		signature.setSignDate(DateUtils.getCurrentCalendarWithNoTime());

		document.addSignature(signature);
		document.setDocumentId(documentId);
		try (FileOutputStream fos = new FileOutputStream(copyPath.toFile())) {
			ExternalSigningSupport externalSigningSupport = document.saveIncrementalForExternalSigning(fos);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] content = externalSigningSupport.getContent().readAllBytes();
			document.close();
			return Base64.getEncoder().encodeToString(md.digest(content));
		} catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

	private void assertDocumentCreator(List<Long> documentsIds, Integer userId) {
		documentsIds.forEach(documentId -> {
			if (!documentFileStorage.isDocumentBelongsToUser(documentId, userId))
				throw new SignDocumentFileException(SignDocumentFileEnumException.WRONG_DOCUMENT_CREATOR, String.format("El usuario no es el creador del documento %s", documentId));
		});
	}

	private String getNewRelativeDirectory(String path, String realFileName) {
		int lastSlashIndex = path.lastIndexOf("/");
		return path.substring(0, lastSlashIndex) + "/" + realFileName + ".pdf";
	}

}
