package ar.lamansys.sgh.clinichistory.mocks;

import ar.lamansys.sgh.shared.infrastructure.output.entities.ESignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;

public class DocumentFileTestMocks {

	public static DocumentFile createDocumentFile(Long documentId, Integer internmentEpisodeId, Short type, Short sourceType, String filename, String filepath,
												  String uuidFile) {
		DocumentFile result = new DocumentFile();
		result.setId(documentId);
		result.setSourceId(internmentEpisodeId);
		result.setTypeId(type);
		result.setSourceTypeId(sourceType);
		result.setFilename(filename);
		result.setFilepath(filepath);
		result.setUuidfile(uuidFile);
		result.setSignatureStatusId(ESignatureStatus.PENDING.getId());
		return result;
	}

}
